package com.wms.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Reads framework configuration from config.properties.
 * <p>
 * All properties are loaded once at startup and cached. Missing keys return
 * sensible defaults where possible; callers should {@code requireNonNull}
 * for critical values like {@code app.url}.
 */
public final class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final String PROPERTIES_FILE = "config.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is == null) {
                log.warn("{} not found on classpath — using defaults", PROPERTIES_FILE);
            } else {
                props.load(is);
                log.info("Loaded {} properties from {}", props.size(), PROPERTIES_FILE);
            }
        } catch (Exception e) {
            log.error("Failed to load {}", PROPERTIES_FILE, e);
        }
    }

    private ConfigReader() {
    }

    // --- App ---

    public static String getAppUrl() {
        return get("app.url");
    }

    public static String getAppTitle() {
        return get("app.title", "WMS - Login");
    }

    // --- Browser ---

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public static int getImplicitWaitMs() {
        return parseInt("implicit.wait.ms", 5000);
    }

    public static int getExplicitWaitSeconds() {
        return parseInt("explicit.wait.seconds", 10);
    }

    public static int getPageLoadTimeoutSeconds() {
        return parseInt("page.load.timeout.seconds", 30);
    }

    // --- Credentials ---

    public static String getUsername() {
        return get("username");
    }

    public static String getPassword() {
        return get("password");
    }

    public static String getCompany() {
        return get("company");
    }

    // --- Screenshots ---

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(get("screenshot.on.failure", "true"));
    }

    public static String getScreenshotDir() {
        return get("screenshot.dir", "./screenshots");
    }

    // --- Internal helpers ---

    private static String get(String key) {
        String val = props.getProperty(key);
        if (val == null || val.isBlank()) {
            log.warn("Property '{}' is missing or empty", key);
        }
        return val;
    }

    private static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    private static int parseInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            log.warn("Property '{}' is not a valid integer, using default {}", key, defaultValue);
            return defaultValue;
        }
    }
}
