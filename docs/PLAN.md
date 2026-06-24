# Implementation Plan — WMS Selenium Automation Framework

## Phase 0: Page Reconnaissance ✅ (done)
- [x] Fetched live login page at `https://pg-wms.1gotech.com/index.php?r=site/login`
- [x] Mapped all locators (see `docs/locators.md`)
- [x] Discovered **3 fields** (Account Name + Username + Password) — PRD updated to match

## Phase 1: Project Skeleton
- [ ] `pom.xml` with Maven dependencies (Selenium 4, TestNG, WebDriverManager, Log4j2, Allure, AssertJ)
- [ ] Directory structure: `src/main/java/com/wms/{base,pages,utils}`, `src/test/java/com/wms/tests`
- [ ] `.gitignore` update, `README.md`

## Phase 2: Base Layer
- [ ] `ConfigReader` — loads `config.properties`
- [ ] `WebDriverFactory` — thread-safe WebDriver with WebDriverManager
- [ ] `BasePage` — common waits, element interactions, JS helpers
- [ ] `BaseTest` — setup/teardown, screenshot on failure, Allure lifecycle

## Phase 3: Login Module
- [ ] `LoginPage` — POM with all 3 fields (Account Name, Username, Password)
- [ ] `LoginTest` — 8 test scenarios (expanded for 3-field form)
- [ ] DataProvider for invalid combinations

## Phase 4: Utilities & Hardening
- [ ] `ScreenshotUtil` — attach screenshots to Allure on failure
- [ ] Log4j2 logging integration
- [ ] Allure annotations on tests

## Phase 5: Verification
- [ ] Compile and package
- [ ] Run Login suite
- [ ] Fix any flaky locators/waits

## Phase 6: Extensibility (future)
- [ ] Document pattern for new modules
- [ ] Template: copy `pages/login/` → `pages/dashboard/`, `tests/login/` → `tests/dashboard/`

### Excluded (YAGNI / ultra)
- No CI/CD until local runs are solid
- No Docker
- No cross-browser matrix (Chrome only for now)
- No Excel/CSV data sources (DataProvider + config.properties is enough)
- No Lombok
