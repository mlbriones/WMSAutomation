package com.wms.base;

import com.wms.utils.ConfigReader;
import com.wms.utils.ScreenshotUtil;
import com.wms.utils.WebDriverFactory;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

/**
 * Base class for all test classes.
 * <p>
 * Manages WebDriver lifecycle per test method: driver is created before each
 * test and quit after. Thread-safe — uses {@link WebDriverFactory} which stores
 * drivers in a {@link ThreadLocal}. Tests should call {@link #getDriver()} or
 * pass it to Page Objects rather than storing a local reference.
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    @BeforeSuite
    public void beforeSuite() {
        log.info("========================================");
        log.info("WMS Automation Test Suite");
        log.info("Browser: {}", ConfigReader.getBrowser());
        log.info("URL: {}", ConfigReader.getAppUrl());
        log.info("========================================");
    }

    /**
     * Returns the current thread's WebDriver (lazily created).
     * Test methods and Page Objects should use this method instead of
     * storing a driver reference in a field to avoid cross-thread issues
     * during parallel execution.
     */
    protected WebDriver getDriver() {
        return WebDriverFactory.getDriver();
    }

    @BeforeMethod
    public void setUp(Method method) {
        log.info("--- Starting test: {}", method.getName());
        getDriver().get(ConfigReader.getAppUrl());
    }

    @AfterMethod
    public void tearDown(Method method, ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            log.error("Test FAILED: {}", method.getName());
            captureScreenshot();
        } else if (ITestResult.SUCCESS == result.getStatus()) {
            log.info("Test PASSED: {}", method.getName());
        } else {
            log.warn("Test SKIPPED: {}", method.getName());
        }
        WebDriverFactory.quitDriver();
    }

    @AfterSuite
    public void afterSuite() {
        log.info("========================================");
        log.info("Test Suite Completed");
        log.info("========================================");
    }

    /**
     * Captures a screenshot and attaches it to the Allure report.
     */
    private void captureScreenshot() {
        try {
            byte[] screenshotBytes = ScreenshotUtil.takeScreenshot(getDriver());
            if (screenshotBytes != null) {
                Allure.addAttachment(
                        "Screenshot - " + Thread.currentThread().getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshotBytes),
                        ".png"
                );
            }
        } catch (Exception e) {
            log.error("Failed to capture screenshot", e);
        }
    }
}
