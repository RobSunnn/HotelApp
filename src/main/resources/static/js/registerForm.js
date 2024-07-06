document.getElementById("register-button").addEventListener('click', encryptAndSubmit);

async function encryptAndSubmit() {
    try {
        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const age = document.getElementById('age').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        let encryptedEmail = await encryptData(email);
        let encryptedPassword = await encryptData(password);
        let encryptedConfirmPassword = await encryptData(confirmPassword);

        // Prepare the data to send
        const formData = new FormData();
        formData.append('firstName', firstName);
        formData.append('lastName', lastName);
        formData.append('email', encryptedEmail);
        formData.append('age', age);
        formData.append('password', encryptedPassword);
        formData.append('confirmPassword', encryptedConfirmPassword);

        // Send the encrypted data
        const response = await fetch('http://localhost:8080/users/register', {
            method: 'POST',
            body: formData,
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
            }
        });

        const responseData = await response.json();

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
