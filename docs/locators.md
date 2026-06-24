# WMS Login Page — Locator Map

**URL:** `https://pg-wms.1gotech.com/index.php?r=site/login`
**Framework:** Yii PHP with `yiiactiveform` (AJAX client-side validation)

## Fields

| Element | ID | Name | CSS | Notes |
|---------|----|------|-----|-------|
| Account Name | `LoginForm_company` | `LoginForm[company]` | `#LoginForm_company` | ✅ Use ID |
| Username | `LoginForm_username` | `LoginForm[username]` | `#LoginForm_username` | ✅ Use ID |
| Password | `LoginForm_password` | `LoginForm[password]` | `#LoginForm_password` | ✅ Use ID |
| Remember Me | `LoginForm_rememberMe` | `LoginForm[rememberMe]` | `#LoginForm_rememberMe` | Checkbox |
| Sign In button | *(none)* | `yt0` | `input[value='Sign me in']` | Use CSS selector |

## Error Messages (per-field, hidden by default)

| For Field | Error Element ID | CSS Selector |
|-----------|-----------------|--------------|
| Account Name | `LoginForm_company_em_` | `#LoginForm_company_em_` |
| Username | `LoginForm_username_em_` | `#LoginForm_username_em_` |
| Password | `LoginForm_password_em_` | `#LoginForm_password_em_` |

### Client-side validation messages
- Account Name: *"Account Name cannot be blank."*
- Username: *"Username cannot be blank."*
- Password: *"Password cannot be blank."*

## Form
- `<form id="login-form" action="/index.php?r=site/login" method="post">`

## Notes
- Error divs have `style="display:none"` by default — Yii JS reveals them on validation
- No iframes detected
- All IDs are static (Yii convention: `ModelName_attribute`)
