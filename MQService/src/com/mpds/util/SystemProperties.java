
package com.mpds.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SystemProperties {
	private static List<Properties> propList = new LinkedList<Properties>();
	
	/*
	static {
		try {
			loadProperties("system.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	/**
	 * This method returns the property values, if found. It loads the properties from the map properties file if not
	 * already loaded.
	 * 
	 * @param String property the property whose value is requested for
	 * @param String def the default value to return if property not found
	 * @return String retVal the value returned
	 */
	public static String getProperty(String property, String def){
		String retVal = def;
		for (Iterator<Properties> iter = propList.iterator(); iter.hasNext();) {
			Properties prop = iter.next();
			if (!prop.containsKey(property)) {
				continue;
			}
			retVal = prop.getProperty(property, def) == null ? def : prop.getProperty(property, def).trim();
		}
		return retVal;
	}

	/**
	 * sets the property for the given key & value
	 * 
	 * @param strKey String Key
	 * @param strValue String Value
	 * @return void
	 * 
	 **/
	public static void setProperty(String strKey, String strValue){
		if (propList.size() > 0) {
			propList.get(0).setProperty(strKey, strValue);
		} else {
		    Properties prop = new Properties();
		    prop.setProperty(strKey, strValue);
		    propList.add(prop);
		}
	}

	/**
	 * This method loads the Properties object from the Map Properties file
	 * 
	 * @param String file the configuration file
	 */
	public static void loadProperties(String file) throws Exception{
		propList.add(loadPropertiesFile(file));
	}

	/**
	 * This method loads a Properties object and returns it
	 * 
	 * @param String file the configuration file to load
	 */
	public static Properties loadPropertiesFile(String file) throws Exception{
		Properties retVal = new Properties();
		InputStream in = SystemProperties.class.getClassLoader().getResourceAsStream(file);
		if (in != null) {
			retVal.load(in);
		}
		in.close();
		return retVal;
	}
	
	/**
	 * Get the String array value from the properties file with the default delimiter ","
	 * @param paramString    The key
	 * @return
	 */
	public static String[] getStringArray(String paramString) {
		String str = getProperty(paramString, "");
		if ("".equals(str)) {
			return null;
		}
		return str.split(",");
	}

	public static Map<String, String> getKeyValue(String paramString) {
		String str = getProperty(paramString, "");
		if ("".equals(str)) {
			return new HashMap<String, String>();
		}
		Map<String, String> keyVal = new HashMap<String, String>();
		String[] values = str.split(",");
		for (String value : values) {
			keyVal.put(value.split(":")[0], value.split(":")[1]);
		}
		return keyVal;
	}
	
	public static int getInt(String strKey, int defaultVal) {
		String str = getProperty(strKey, "0");
		int val = Integer.valueOf(str);
		if (val == 0) {
			return defaultVal;
		}
		return val;
	}
}
