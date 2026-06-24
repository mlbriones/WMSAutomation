package com.wms.pages.login;

import com.wms.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the WMS Login page.
 * <p>
 * The login form has three required fields: Account Name, Username, and
 * Password. Error messages appear per-field in hidden {@code <div>} elements
 * that Yii reveals on validation failure.
 */
public class LoginPage extends BasePage {

    // ========== Locators ==========

    private static final By COMPANY_INPUT = By.id("LoginForm_company");
    private static final By USERNAME_INPUT = By.id("LoginForm_username");
    private static final By PASSWORD_INPUT = By.id("LoginForm_password");
    private static final By REMEMBER_ME_CHECKBOX = By.id("LoginForm_rememberMe");
    private static final By SIGN_IN_BUTTON = By.cssSelector("input[value='Sign me in']");

    private static final By COMPANY_ERROR = By.id("LoginForm_company_em_");
    private static final By USERNAME_ERROR = By.id("LoginForm_username_em_");
    private static final By PASSWORD_ERROR = By.id("LoginForm_password_em_");

    private static final By LOGIN_FORM = By.id("login-form");

    // ========== Constructor ==========

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ========== Page actions ==========

    @Step("Enter company: {company}")
    public LoginPage enterCompany(String company) {
        type(COMPANY_INPUT, company);
        return this;
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        type(USERNAME_INPUT, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    @Step("Click Sign In button")
    public LoginPage clickSignIn() {
        click(SIGN_IN_BUTTON);
        return this;
    }

    /**
     * Performs a full login in one call — convenience for the happy path.
     *
     * @param company  Account Name
     * @param username Username
     * @param password Password
     * @return the LoginPage (in case of failure) or a future DashboardPage
     */
    @Step("Login with company: {company}, username: {username}")
    public LoginPage loginAs(String company, String username, String password) {
        enterCompany(company);
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
        return this;
    }

    // ========== Getters ==========

    public String getCompanyError() {
        return getTextIfVisible(COMPANY_ERROR);
    }

    public String getUsernameError() {
        return getTextIfVisible(USERNAME_ERROR);
    }

    public String getPasswordError() {
        return getTextIfVisible(PASSWORD_ERROR);
    }

    // ========== State checks ==========

    public boolean isAnyErrorDisplayed() {
        return isDisplayed(COMPANY_ERROR)
                || isDisplayed(USERNAME_ERROR)
                || isDisplayed(PASSWORD_ERROR);
    }

    public boolean isCompanyErrorDisplayed() {
        return isDisplayed(COMPANY_ERROR);
    }

    public boolean isUsernameErrorDisplayed() {
        return isDisplayed(USERNAME_ERROR);
    }

    public boolean isPasswordErrorDisplayed() {
        return isDisplayed(PASSWORD_ERROR);
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed(LOGIN_FORM);
    }

    /**
     * @return true if we're still on the login page (form visible or URL contains "login")
     */
    public boolean isOnLoginPage() {
        waitForPageLoad();
        return isDisplayed(LOGIN_FORM) || getCurrentUrl().toLowerCase().contains("login");
    }

    /**
     * Checks whether login succeeded by verifying we've navigated away from
     * the login page. The post-login URL typically no longer contains "login".
     */
    public boolean isLoginSuccessful() {
        waitForPageLoad();
        return !getCurrentUrl().toLowerCase().contains("login");
    }
}
