# Blood Bridge - e-Rakt Kosh Clone

A complete clone of the e-Rakt Kosh blood donation portal website, featuring both donor login and registration pages with full functionality.

## Features

- **Complete Website**: Login page and registration page with navigation
- **Responsive Design**: Works perfectly on desktop, tablet, and mobile devices
- **Form Validation**: Real-time validation for all required fields
- **Interactive Captcha**: Clickable captcha that regenerates on click
- **OTP Simulation**: Mobile OTP generation for login
- **State-District Mapping**: Dynamic district selection based on state
- **Professional UI**: Clean, modern design matching the original portal
- **Accessibility**: Full accessibility features and controls

## Website Structure

- `index.html` - Redirects to login page
- `login.html` - Main login page with OTP functionality
- `register.html` - Donor registration page
- `styles.css` - Complete CSS styling for both pages
- `script.js` - JavaScript functionality for both pages

## Pages

### 1. Login Page (`login.html`)
- Mobile number input with OTP generation
- Registration benefits display
- "Register Now" button linking to registration page
- Complete footer with all navigation links

### 2. Registration Page (`register.html`)
- Complete donor registration form
- Two-column layout with all required fields
- Interactive captcha verification
- Form validation and submission

## Form Fields (Registration)

### Required Fields (*)
- Name
- Gender
- Mobile Number
- State
- Age
- Father Name
- District
- PinCode
- Captcha

### Optional Fields
- Email
- Address

## Features

### Login Page
- **Mobile Validation**: 10-digit mobile number validation
- **OTP Generation**: Simulated OTP sending with 30-second cooldown
- **Navigation**: Direct link to registration page

### Registration Page
- **Real-time Validation**: Instant feedback on form inputs
- **Mobile Number**: Must be 10 digits starting with 6-9
- **Age**: Must be between 18-65 years
- **Email**: Valid email format (if provided)
- **PinCode**: Must be 6 digits
- **Captcha**: Must match the displayed text
- **State-District**: Dynamic district loading based on selected state

## How to Use

1. Open `index.html` in a web browser (redirects to login)
2. **Login Page**: Enter mobile number and click "Generate OTP"
3. **Registration Page**: Click "Register Now" to access the registration form
4. Fill out all required fields with proper validation
5. Complete the captcha verification
6. Submit the form

## Browser Compatibility

- Chrome (recommended)
- Firefox
- Safari
- Edge

## Responsive Breakpoints

- Desktop: 1200px and above
- Tablet: 768px - 1199px
- Mobile: Below 768px

## Project Name

**Blood Bridge** - A complete e-Rakt Kosh clone with modern web technologies
