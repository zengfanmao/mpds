package com.mpds.persistence;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.mpds.persistence.mapper.QueryMapper;
import com.mpds.persistence.po.TbUserIMDatas;
import com.mpds.util.SystemProperties;

public class DBAccessUtils {

	private final Logger logger = Logger.getLogger(getClass());
	
	private static DBAccessUtils instance;
	private SqlSessionFactory factory;
	
	private DBAccessUtils() {
		LogFactory.useLog4JLogging();
		String resource = "SqlMapConfig.xml";
		String datasource = SystemProperties.getProperty("mqservice.datasources","HA");
		try {
			InputStream reader = Resources.getResourceAsStream(resource);
			factory = new SqlSessionFactoryBuilder().build(reader, datasource);
			factory.getConfiguration().addMapper(QueryMapper.class);
		} catch (IOException e) {
			logger.error("Initilizing Data Access Util with exception : ", e);
		}
	}
	
	public static DBAccessUtils getInstance() {
		if (instance == null) {
			synchronized (DBAccessUtils.class) {
				if (instance == null) {
					instance = new DBAccessUtils();
				}
			}
		}
		return instance;
	}
	
	public void insertUserGps(TbUserIMDatas param) {
		if (factory == null) {
			return;
		}
		SqlSession session = factory.openSession(true);
		QueryMapper query = session.getMapper(QueryMapper.class);
		try {
			String uCode = query.selectUserGps(param);
			if (StringUtils.isBlank(uCode)) {
				query.insertUserGps(param);
			} else {
				query.updateUserGps(param);
			}
		} catch (Exception e) {
			logger.error("Insert User GPS with exception : ", e);
		} finally {
			session.close();
		}
	}
	
	public void updateUserState(TbUserIMDatas param) {
		if (factory == null) {
			return;
		}
		SqlSession session = factory.openSession(true);
		QueryMapper query = session.getMapper(QueryMapper.class);
		try {
			query.updatePdtUserStatus(param);
		} catch (Exception e) {
			logger.error("Update User State with exception : ", e);
		} finally {
			session.close();
		}
	}
	
	public void updateUserGroup(TbUserIMDatas param) {
		if (factory == null) {
			return;
		}
		SqlSession session = factory.openSession(true);
		QueryMapper query = session.getMapper(QueryMapper.class);
		try {
			String groupName = query.selectGroupName(param);
			param.setGroupName(groupName);
			query.updatePdtUserGroup(param);
		} catch (Exception e) {
			logger.error("Update User Group with exception : ", e);
		} finally {
			session.close();
		}
	}
}
