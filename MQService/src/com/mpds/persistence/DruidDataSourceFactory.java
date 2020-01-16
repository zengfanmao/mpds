package com.mpds.persistence;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDataSourceFactory implements DataSourceFactory {

	private Properties props;
	@Override
	public DataSource getDataSource() {
		DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(this.props.getProperty("driver"));
        dds.setUrl(this.props.getProperty("url"));
        dds.setUsername(this.props.getProperty("username"));
        dds.setPassword(this.props.getProperty("password"));
        dds.setMaxActive(Integer.valueOf(this.props.getProperty("poolMaximumActiveConnections")));
        dds.setMinIdle(Integer.valueOf(this.props.getProperty("poolMaximumIdleConnections")));
        try {
            dds.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dds;
	}

	@Override
	public void setProperties(Properties props) {
		this.props = props;
	}

}
