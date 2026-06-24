# OpenCode Rules - Selenium Java Framework

## Technology Stack
- Java 17+
- Selenium 4.XX
- TestNG
- Maven
- Log4j2
- Allure Reporting (optional)
- WebDriverManager

## Architecture Rules
- Use **Page Object Model** (POM)
- BasePage + BaseTest pattern
- Module-wise folder structure (`pages/login`, `pages/dashboard`, etc.)
- Repository + Utility separation
- Explicit waits everywhere
- Configurable locators

## Quality Rules
- Every page must have corresponding test class
- Proper assertions and reporting
- Screenshot on failure
- Parallel execution support
- Minimum 80% test coverage

## Coding Standards
- Clean Java code with proper naming
- Meaningful comments and JavaDoc
- No hard-coded values (use config/properties)
- Thread-safe WebDriver handling