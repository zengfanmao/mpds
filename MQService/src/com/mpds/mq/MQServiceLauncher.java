package com.mpds.mq;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mpds.persistence.DBAccessUtils;
import com.mpds.util.SystemProperties;

public class MQServiceLauncher {
	
	private static final Logger logger = Logger.getLogger(MQServiceLauncher.class);

	public static void main(String[] args) {
		System.setProperty("mqservice.root", args.length > 0?args[0]:System.getProperty("user.dir"));
		try {
			String sysconfig = args.length > 1?args[1]:"system.properties";
			String log4jfile = args.length > 2?args[2]:"log4j.properties";
			SystemProperties.loadProperties(sysconfig);
			PropertyConfigurator.configure(SystemProperties.loadPropertiesFile(log4jfile));
		} catch (Exception e) {
			System.err.println("Initialize the resource with error." + e.getMessage());
			logger.error("Initialize the resource with error.", e);
		}
		MQServiceLauncher launcher = new MQServiceLauncher();
		launcher.launch();
		DBAccessUtils.getInstance();
	}
	
	public void launch() {
		Thread gis = new Thread(new GISService());
		gis.start();
		Thread state = new Thread(new StateService());
		state.start();
	}
}
