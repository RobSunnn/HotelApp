document.getElementById("register-button").addEventListener('click', encryptAndSubmit);

async function encryptAndSubmit() {
    try {
        const key = CryptoJS.lib.WordArray.random(32); // 256-bit key
        const iv = CryptoJS.lib.WordArray.random(16);
        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const age = document.getElementById('age').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        const encryptedEmail = CryptoJS.AES.encrypt(email, key, {iv: iv}).toString();
        const encryptedPassword = CryptoJS.AES.encrypt(password, key, {iv: iv}).toString();
        const encryptedConfirmPassword = CryptoJS.AES.encrypt(confirmPassword, key, {iv: iv}).toString();

        // Prepare the data to send
        const formData = new FormData();
        formData.append('firstName', firstName);
        formData.append('lastName', lastName);
        formData.append('email', encryptedEmail);
        formData.append('age', age);
        formData.append('password', encryptedPassword);
        formData.append('confirmPassword', encryptedConfirmPassword);
        formData.append('iv', CryptoJS.enc.Base64.stringify(iv));
        formData.append('key', CryptoJS.enc.Base64.stringify(key));

        // Send the encrypted data
        const response = await fetch('http://localhost:8080/users/register', {
            method: 'POST',
            body: formData,
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
            }
        });

        const responseData = await response.json();
        console.log(responseData);

        if (responseData.success) {
            if (responseData.redirectUrl) {
                // Redirect to the success page
                window.location.href = responseData.redirectUrl;
            }
        } else {
            if (responseData.errors) {
                displayErrors(responseData.errors);
            }
        }
    } catch (error) {
        console.error('Error during registration:', error);
    }
}

function displayErrors(errors) {
    // Clear previous errors
    document.querySelectorAll('.invalid-feedback').forEach(el => el.textContent = '');
    document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

    if (errors.length > 0) {
        // Scroll to the first error
        const firstErrorField = errors[0].field;
        const firstErrorElement = document.getElementById(firstErrorField);
        if (firstErrorElement) {
            firstErrorElement.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
    }

    // Iterate over the errors array
    errors.forEach(error => {
        const errorElement = document.getElementById(`${error.field}Error`);
        const inputElement = document.getElementById(error.field);
        if (errorElement && inputElement) {
            errorElement.textContent = error.defaultMessage;
            inputElement.classList.add('is-invalid');
        }
    });
}



