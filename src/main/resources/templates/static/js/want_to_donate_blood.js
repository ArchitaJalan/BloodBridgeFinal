// Form functionality and captcha generation
document.addEventListener('DOMContentLoaded', function() {
    // Check if we're on login page or registration page
    const isLoginPage = document.querySelector('.login-container') !== null;
    const isRegisterPage = document.querySelector('.donor-form') !== null;
    // Generate random captcha
    function generateCaptcha() {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for (let i = 0; i < 7; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    }

    // Update captcha display
    function updateCaptcha() {
        const captchaDisplay = document.getElementById('captchaDisplay');
        const newCaptcha = generateCaptcha();
        captchaDisplay.textContent = newCaptcha;
        return newCaptcha;
    }

    // Initialize captcha for registration page
    let currentCaptcha = '';
    if (isRegisterPage) {
        currentCaptcha = updateCaptcha();

        // Add click event to captcha to regenerate
        document.getElementById('captchaDisplay').addEventListener('click', function() {
            currentCaptcha = updateCaptcha();
            document.getElementById('captcha').value = '';
        });
    }

    // Form validation
    function validateForm(formData) {
        const errors = [];

        // Name validation
        if (!formData.name || formData.name.trim().length < 2) {
            errors.push('Name must be at least 2 characters long');
        }

        // Age validation
        const age = parseInt(formData.age);
        if (!age || age < 18 || age > 65) {
            errors.push('Age must be between 18 and 65');
        }

        // Mobile validation
        const mobileRegex = /^[6-9]\d{9}$/;
        if (!formData.mobile || !mobileRegex.test(formData.mobile)) {
            errors.push('Please enter a valid 10-digit mobile number');
        }

        // Email validation (if provided)
        if (formData.email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(formData.email)) {
                errors.push('Please enter a valid email address');
            }
        }

        // Pincode validation
        const pincodeRegex = /^\d{6}$/;
        if (!formData.pincode || !pincodeRegex.test(formData.pincode)) {
            errors.push('Please enter a valid 6-digit pincode');
        }

        // Captcha validation
        if (!formData.captcha || formData.captcha !== currentCaptcha) {
            errors.push('Please enter the correct captcha');
        }

        return errors;
    }

    // Show error messages
    function showErrors(errors) {
        // Remove existing error messages
        const existingErrors = document.querySelectorAll('.error-message');
        existingErrors.forEach(error => error.remove());

        // Show new error messages
        if (errors.length > 0) {
            const errorContainer = document.createElement('div');
            errorContainer.className = 'error-container';
            errorContainer.style.cssText = `
                background: #fee2e2;
                border: 1px solid #fca5a5;
                color: #dc2626;
                padding: 15px;
                border-radius: 6px;
                margin-bottom: 20px;
            `;

            const errorList = document.createElement('ul');
            errorList.style.cssText = `
                margin: 0;
                padding-left: 20px;
            `;

            errors.forEach(error => {
                const errorItem = document.createElement('li');
                errorItem.textContent = error;
                errorItem.className = 'error-message';
                errorList.appendChild(errorItem);
            });

            errorContainer.appendChild(errorList);
            
            // Insert before the form
            const form = document.getElementById('donorForm');
            form.parentNode.insertBefore(errorContainer, form);
        }
    }

    // Show success message
    function showSuccess() {
        const successContainer = document.createElement('div');
        successContainer.className = 'success-container';
        successContainer.style.cssText = `
            background: #d1fae5;
            border: 1px solid #86efac;
            color: #059669;
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            text-align: center;
        `;
        successContainer.innerHTML = `
            <h3>Registration Successful!</h3>
            <p>Thank you for registering as a blood donor. Your information has been submitted successfully.</p>
        `;

        const form = document.getElementById('donorForm');
        form.parentNode.insertBefore(successContainer, form);
        
        // Reset form after 3 seconds
        setTimeout(() => {
            form.reset();
            currentCaptcha = updateCaptcha();
            successContainer.remove();
        }, 3000);
    }

    // Login page functionality
    if (isLoginPage) {
        // OTP generation
        document.getElementById('generateOTP').addEventListener('click', function() {
            const mobileInput = document.getElementById('mobile');
            const mobile = mobileInput.value.trim();
            
            if (!mobile) {
                alert('Please enter your mobile number first');
                mobileInput.focus();
                return;
            }
            
            if (!/^[6-9]\d{9}$/.test(mobile)) {
                alert('Please enter a valid 10-digit mobile number');
                mobileInput.focus();
                return;
            }
            
            // Simulate OTP generation
            const otp = Math.floor(100000 + Math.random() * 900000);
            alert(`OTP sent to ${mobile}: ${otp}\n\n(Note: This is a demo. In real implementation, OTP would be sent via SMS)`);
            
            // Disable button temporarily
            this.disabled = true;
            this.textContent = 'OTP Sent';
            this.style.background = '#6c757d';
            
            // Re-enable after 30 seconds
            setTimeout(() => {
                this.disabled = false;
                this.textContent = 'Generate OTP';
                this.style.background = '#dc2626';
            }, 30000);
        });
        
        // Mobile number formatting
        document.getElementById('mobile').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 10) {
                value = value.substring(0, 10);
            }
            e.target.value = value;
        });
    }

    // Registration page functionality
    if (isRegisterPage) {
        // Form submission
        document.getElementById('donorForm').addEventListener('submit', function(e) {
            e.preventDefault();

            // Remove existing error messages
            const existingErrors = document.querySelectorAll('.error-container');
            existingErrors.forEach(error => error.remove());

            // Get form data
            const formData = {
                name: document.getElementById('name').value.trim(),
                gender: document.getElementById('gender').value,
                mobile: document.getElementById('mobile').value.trim(),
                state: document.getElementById('state').value,
                address: document.getElementById('address').value.trim(),
                age: document.getElementById('age').value,
                fatherName: document.getElementById('fatherName').value.trim(),
                email: document.getElementById('email').value.trim(),
                district: document.getElementById('district').value,
                pincode: document.getElementById('pincode').value.trim(),
                captcha: document.getElementById('captcha').value.trim()
            };

            // Validate form
            const errors = validateForm(formData);

            if (errors.length > 0) {
                showErrors(errors);
            } else {
                // Simulate form submission
                console.log('Form submitted with data:', formData);
                showSuccess();
            }
        });

        // Real-time validation for mobile number
        document.getElementById('mobile').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, ''); // Remove non-digits
            if (value.length > 10) {
                value = value.substring(0, 10);
            }
            e.target.value = value;
        });

        // Real-time validation for pincode
        document.getElementById('pincode').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, ''); // Remove non-digits
            if (value.length > 6) {
                value = value.substring(0, 6);
            }
            e.target.value = value;
        });

        // Real-time validation for age
        document.getElementById('age').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, ''); // Remove non-digits
            if (value.length > 2) {
                value = value.substring(0, 2);
            }
            e.target.value = value;
        });

        // State-District dependency (simplified)
        document.getElementById('state').addEventListener('change', function(e) {
            const districtSelect = document.getElementById('district');
            const state = e.target.value;
            
            // Clear existing options
            districtSelect.innerHTML = '<option value="">Select District</option>';
            
            // Add districts based on state (simplified mapping)
            const districts = {
                'karnataka': [
                    'Bangalore Urban', 'Bangalore Rural', 'Mysore', 'Mangalore', 
                    'Hubli', 'Belgaum', 'Gulbarga', 'Davangere'
                ],
                'maharashtra': [
                    'Mumbai', 'Pune', 'Nagpur', 'Nashik', 'Aurangabad', 'Solapur'
                ],
                'tamil-nadu': [
                    'Chennai', 'Coimbatore', 'Madurai', 'Tiruchirappalli', 'Salem'
                ],
                'delhi': ['Central Delhi', 'East Delhi', 'New Delhi', 'North Delhi', 'South Delhi']
            };
            
            if (districts[state]) {
                districts[state].forEach(district => {
                    const option = document.createElement('option');
                    option.value = district.toLowerCase().replace(/\s+/g, '-');
                    option.textContent = district;
                    districtSelect.appendChild(option);
                });
            }
        });

        // Add some visual feedback for form interactions
        const inputs = document.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('focus', function() {
                this.style.borderColor = '#dc2626';
                this.style.background = 'white';
            });
            
            input.addEventListener('blur', function() {
                if (!this.value) {
                    this.style.borderColor = '#d1d5db';
                    this.style.background = '#f9fafb';
                }
            });
        });
    }
});
