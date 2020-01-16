package com.mpds.mq;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.mpds.persistence.DBAccessUtils;
import com.mpds.persistence.po.TbUserIMDatas;
import com.mpds.util.SystemProperties;

public class GISService implements Runnable {

	@Override
	public void run() {
		this.launch();
	}
	
	private String mqUrl = SystemProperties.getProperty("mqservice.activemq.url", "tcp://127.0.0.1:61616");
	private String mqTopic = SystemProperties.getProperty("mqservice.topic.vt_rgis", "VT_RGIS");
	private final Logger logger = Logger.getLogger(getClass());
	private final Object threadObj = new Object();
	private final ExecutorService pool = new ThreadPoolExecutor(SystemProperties.getInt("mqservice.topic.threadpool", 2000), SystemProperties.getInt("mqservice.topic.maxpool", 3000), 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(SystemProperties.getInt("mqservice.topic.maxpool", 3000)), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
	
	public void launch() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqUrl);
		factory.getPrefetchPolicy().setTopicPrefetch(100);
		factory.setOptimizeAcknowledge(true);
		factory.setOptimizeAcknowledgeTimeOut(3000);
		while (true) {
			synchronized (threadObj) {
				Connection connection = null;
				try {
					logger.info("Start MQ GIS Handling .... ");
					connection = factory.createConnection();
					connection.start();
					Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
					Topic topic = session.createTopic(mqTopic);
					MessageConsumer consumer = session.createConsumer(topic);
					consumer.setMessageListener(new MQMessageListener());
					connection.setExceptionListener(new MQExceptionListener());
					threadObj.wait();
				} catch (Exception e) {
					logger.error("Active MQ GIS Connect Error : ", e);
				} finally {
					if (connection != null) {
						try {
							connection.close();
							logger.info("End MQ GIS Handling .... ");
						} catch (Exception e) {
							logger.error("Active MQ GIS Disconnect Error : ", e);
						}
					}
				}
			}
		}
	}
	
	private class MQMessageListener implements MessageListener {
		
		@Override
		public void onMessage(Message msg) {
			try {
				pool.submit(new MessageHandle(msg));
			} catch (Exception e) {
				logger.error("Message Handle with Error : ", e);
			}
		}
	}
	
private class MessageHandle implements Callable<String> {
		
		private Logger logger = Logger.getLogger(MessageHandle.class);
		private Pattern pattern = Pattern.compile("[0-9]\\d*\\.\\d*");
		private Pattern positive = Pattern.compile("[0-9]{7,10}");
		private Message msg;
		
		public MessageHandle(Message msg) {
			this.msg = msg;
		}
		
		@Override
		public String call() {
			try {
				long start = System.currentTimeMillis();
				if (msg instanceof TextMessage) {
					TextMessage txtMsg = (TextMessage) msg;
					logger.debug("Received GIS : " + txtMsg.getText());
					Matcher matcher = pattern.matcher(txtMsg.getText());
					TbUserIMDatas gps = new TbUserIMDatas();
					if (matcher.find()) {
						String n = matcher.group();
						float nVal = Integer.valueOf(n.substring(0, 2)) + Float.valueOf(n.substring(2)) / 60;
						gps.setLatitude(nVal);
					}
					if (matcher.find()) {
						String e = matcher.group();
			            float eVal = Integer.valueOf(e.substring(0, 3)) + Float.valueOf(e.substring(3)) / 60;
						gps.setLongitude(eVal);
					}
					matcher = positive.matcher(txtMsg.getText());
					if (matcher.find()) {
						gps.setuCode(matcher.group());
					}
					gps.setType("PDT");
					gps.setLastModifyTime(Calendar.getInstance().getTime());
					
					logger.debug("Received GIS Json : " + gps);
					DBAccessUtils.getInstance().insertUserGps(gps);
					logger.debug("Handled GIS : " + txtMsg.getText());
				}
				long end = System.currentTimeMillis();
				logger.info(Thread.currentThread() + "" + (end - start));
				return Thread.currentThread() + msg.getJMSMessageID();
			} catch (Exception e) {
				logger.error("Received GIS Error : ", e);
				return Thread.currentThread() + "errormsg";
			}
		}
	}
	
	private class MQExceptionListener implements ExceptionListener {

		@Override
		public void onException(JMSException arg0) {
			synchronized (threadObj) {
				threadObj.notify();
			}
		}
	}
}
