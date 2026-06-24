package com.wms.tests.login;

import com.wms.base.BaseTest;
import com.wms.pages.login.LoginPage;
import com.wms.utils.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the WMS Login module.
 * <p>
 * Covers all login scenarios from the PRD, expanded to include the
 * Account Name (company) field discovered during page reconnaissance.
 *
 * @see LoginPage
 */
@Story("Login")
public class LoginTest extends BaseTest {

    // ========== Data Providers ==========

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"", "", "", "Account Name cannot be blank."},
                {"ValidCo", "", "", "Username cannot be blank."},
                {"ValidCo", "validUser", "", "Password cannot be blank."},
                {"", "validUser", "validPass", "Account Name cannot be blank."},
                {"ValidCo", "", "validPass", "Username cannot be blank."},
                {"", "", "validPass", "Account Name cannot be blank."},
                {"BadCo", "badUser", "badPass", null},
        };
    }

    // ========== Tests ==========

    @Test(description = "Successful login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can log in with valid Account Name, Username, and Password")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(
                ConfigReader.getCompany(),
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );

        assertThat(loginPage.isLoginSuccessful())
                .as("Should redirect away from login page on success")
                .isTrue();
    }

    @Test(description = "Failed login - invalid password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error is shown when password is incorrect")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(ConfigReader.getCompany(), ConfigReader.getUsername(), "wrongPass");

        assertThat(loginPage.isOnLoginPage())
                .as("Should stay on login page with invalid password")
                .isTrue();
    }

    @Test(description = "Failed login - invalid username")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error is shown when username is incorrect")
    public void testInvalidUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(ConfigReader.getCompany(), "nonexistent_user", ConfigReader.getPassword());

        assertThat(loginPage.isOnLoginPage())
                .as("Should stay on login page with invalid username")
                .isTrue();
    }

    @Test(description = "Failed login - empty fields validation",
            dataProvider = "invalidCredentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify client-side validation errors for empty or invalid field combinations")
    public void testEmptyFieldsValidation(String company, String username,
                                          String password, String expectedError) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(company, username, password);

        if (expectedError != null) {
            assertThat(loginPage.isOnLoginPage())
                    .as("Should stay on login page when required fields are empty")
                    .isTrue();
        } else {
            assertThat(loginPage.isOnLoginPage())
                    .as("Should remain on login page with invalid credentials")
                    .isTrue();
        }
    }

    @Test(description = "Verify login page loads correctly")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the login page loads with the correct title and form is displayed")
    public void testLoginPageLoads() {
        LoginPage loginPage = new LoginPage(getDriver());

        assertThat(getDriver().getTitle())
                .as("Login page title")
                .isEqualTo(ConfigReader.getAppTitle());

        assertThat(loginPage.isLoginFormDisplayed())
                .as("Login form should be displayed")
                .isTrue();
    }

    @Test(description = "Verify successful login redirect URL")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that after successful login, the URL no longer contains 'login'")
    public void testSuccessfulLoginRedirect() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(
                ConfigReader.getCompany(),
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );

        assertThat(loginPage.isLoginSuccessful())
                .as("Should redirect away from login on success")
                .isTrue();

        assertThat(loginPage.getCurrentUrl())
                .as("Post-login URL should not contain 'login'")
                .doesNotContainIgnoringCase("login");
    }
}
