package com.teradata.common.config;

import java.io.*;
import java.util.Properties;

/**
 * Created by alex on 14-10-31.
 */
public class Configuration {
    private static Properties properties = new Properties();

    static {
        load();
    }

    public static void load() {
        String path = Configuration.class.getClassLoader().getResource("/config/application.properties").getPath();
        InputStream is = null;
        try {
            is = new FileInputStream(new File(path));
            properties.load(new InputStreamReader(is, "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getLong(String key) {
        try {
            return Long.parseLong(get(key));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static long getLong(String key, Long defaultValue) {
        try {
            return Long.parseLong(get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String get(String key) {
        if (properties == null)
            return null;
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : value;
    }

}
