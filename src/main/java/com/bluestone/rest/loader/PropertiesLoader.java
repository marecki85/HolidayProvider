package com.bluestone.rest.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class PropertiesLoader {

	private static Properties properties = null;
	private static String fileName = "config.properties";

	public static String getproperties(String propertyKey) {

		if (properties == null) {
			properties = new Properties();
			try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName)) {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties.getProperty(propertyKey);
	}
}
