package com.wms.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Captures and stores screenshots from a WebDriver instance.
 * <p>
 * Screenshots are stored in the directory defined by
 * {@code screenshot.dir} in config.properties (default: {@code ./screenshots})
 * and are also available as byte arrays for Allure attachment.
 */
public final class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {
    }

    /**
     * Takes a screenshot and returns the raw bytes (for Allure attachment).
     *
     * @param driver the WebDriver instance
     * @return PNG bytes, or {@code null} on failure
     */
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot bytes", e);
            return null;
        }
    }

    /**
     * Saves a screenshot to the configured directory.
     *
     * @param driver the WebDriver instance
     * @param name   a descriptive file name (without extension)
     * @return the absolute path to the saved file, or {@code null} on failure
     */
    public static String saveScreenshot(WebDriver driver, String name) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destDir = Paths.get(ConfigReader.getScreenshotDir());
            Files.createDirectories(destDir);
            Path destPath = destDir.resolve(name + "_" + System.currentTimeMillis() + ".png");
            Files.copy(screenshot.toPath(), destPath);
            log.info("Screenshot saved: {}", destPath.toAbsolutePath());
            return destPath.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", name, e);
            return null;
        }
    }
}
