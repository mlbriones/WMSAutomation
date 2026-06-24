# Product Requirements Document - Selenium Automation Framework

## Project Overview
Build a scalable Selenium Java automation framework for the following website:

**Website Name:** WMS 
**Base URL:** https://pg-wms.1gotech.com/index.php?r=site/login

**Target:** Start with **Login module only**, then make the framework easily extensible for more modules.

## Login Module Requirements

**Test Scenarios to Implement:**
1. Successful login with valid credentials
2. Failed login - invalid username
3. Failed login - invalid password
4. Failed login - both username and password invalid.
5. Login attempt with empty username and password fields
6. Verify successful login redirect
7. Handle any visible error messages on failed login

## Instructions for the AI

- Inspect the login page at the given URL and choose the **best possible locators** (prefer ID → Name → CSS Selector → XPath).
- Use explicit waits (WebDriverWait) for all elements.
- Make all locators **maintainable** (define them clearly in the LoginPage class).
- Follow Page Object Model best practices.
- Design the framework so that adding new modules (Dashboard, etc.) is straightforward.

## Framework Goals
- Production-ready, clean, and scalable
- Support parallel test execution
- Take screenshot on test failure
- Use properties file for configuration
- Proper logging with Log4j2

## Future Extension
The framework should be structured to easily support additional modules later (e.g., Dashboard, Profile, etc.) by adding new packages under `pages/` and `tests/`.