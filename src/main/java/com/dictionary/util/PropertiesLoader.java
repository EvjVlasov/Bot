package com.dictionary.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static final Logger LOG = LogManager.getLogger(PropertiesLoader.class);
    private Properties prop = new Properties();

    public PropertiesLoader() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/Bot.properties")) {
            prop.load(fileInputStream);
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }
    }

    public Properties getProp() {
        return prop;
    }
}