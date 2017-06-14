package com.zbiti.common;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigUtil {

	private static final Log logger=LogFactory.getLog(ConfigUtil.class);
	private static Properties properties = new Properties();
	static {
		try {
			properties.load(ConfigUtil.class.getResourceAsStream("/config/etl.properties"));
		} catch (Exception e) {
			logger.error("配置工具类加载配置出错", e);
		}
	}
	public static String getValueByKey(String key){
		return properties.getProperty(key);
	}
}
