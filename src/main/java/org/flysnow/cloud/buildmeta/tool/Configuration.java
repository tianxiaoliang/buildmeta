package org.flysnow.cloud.buildmeta.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	private static Logger log = Logger.getLogger(Configuration.class);
	private static Properties props = new Properties();

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties props) {
		Configuration.props = props;
	}

	public static void init() {
		props = new Properties();

		try {
			props.load(Configuration.class.getClassLoader()
					.getResourceAsStream("application.properties"));

		} catch (IOException e) {

			log.fatal("property file not found"
					+ ThrowableUtils.stackTrace2String(e));
		}

	}

	public static String getValue(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

}