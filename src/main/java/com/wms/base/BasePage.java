package com.wms.base;

import com.wms.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base class for all Page Objects.
 * <p>
 * Provides common element interaction methods with explicit waits and
 * logging. Subclasses define their own locators and page-specific methods.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Logger log;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        int timeoutSeconds = ConfigReader.getExplicitWaitSeconds();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        this.log = LogManager.getLogger(getClass());
    }

    // ========== Wait wrappers ==========

    protected WebElement waitForVisible(By locator) {
        log.debug("Waiting for visibility of {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForPresent(By locator) {
        log.debug("Waiting for presence of {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected List<WebElement> waitForAllVisible(By locator) {
        log.debug("Waiting for all visible elements: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Checks if an element is displayed, with a short wait (up to 2 s) for JS
     * animations like Yii form validation to reveal hidden error elements.
     */
    protected boolean isDisplayed(By locator) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the text of an element if it is visible, or empty string otherwise.
     * Useful for optional error messages that may or may not be shown.
     */
    protected String getTextIfVisible(By locator) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement el = shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = el.getText();
            log.debug("Got visible text '{}' from {}", text, locator);
            return text != null ? text.trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // ========== Element actions ==========

    protected void click(By locator) {
        click(locator, 2);
    }

    /**
     * Clicks an element, retrying if a StaleElementReferenceException is thrown
     * (e.g. when Yii AJAX validation refreshes the DOM between find and click).
     */
    private void click(By locator, int maxRetries) {
        int attempt = 0;
        while (attempt <= maxRetries) {
            try {
                waitForClickable(locator).click();
                log.info("Clicked {} (attempt {})", locator, attempt + 1);
                return;
            } catch (StaleElementReferenceException e) {
                attempt++;
                if (attempt > maxRetries) {
                    throw e;
                }
                log.debug("Stale element on {} — retrying ({}/{})", locator, attempt, maxRetries);
            }
        }
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        if (text != null) {
            element.sendKeys(text);
        }
        log.info("Typed '{}' into {}", mask(text), locator);
    }

    protected String getText(By locator) {
        String text = waitForVisible(locator).getText();
        log.debug("Got text '{}' from {}", text, locator);
        return text;
    }

    protected String getAttribute(By locator, String attribute) {
        return waitForPresent(locator).getAttribute(attribute);
    }

    protected String getValue(By locator) {
        return getAttribute(locator, "value");
    }

    // ========== JavaScript helpers ==========

    protected void jsClick(By locator) {
        WebElement element = waitForPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        log.info("JS-clicked {}", locator);
    }

    protected void scrollIntoView(By locator) {
        WebElement element = waitForPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // ========== Page load ==========

    protected void waitForPageLoad() {
        wait.until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        log.debug("Page load complete");
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ========== Utility ==========

    private static String mask(String text) {
        if (text == null) return "null";
        // Mask passwords longer than 2 chars for log safety
        return text.length() > 2 ? "******" : text;
    }
}
