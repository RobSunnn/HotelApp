document.addEventListener('DOMContentLoaded', (event) => {
    const successMessage = sessionStorage.getItem('successContactRequestMessage');
    if (successMessage) {
        const messageElement = document.getElementById('successMessage');
        messageElement.querySelector('small').textContent = successMessage;
        messageElement.style.display = 'block';
        sessionStorage.removeItem('successContactRequestMessage');
    }
});

document.getElementById("contact-form").addEventListener("submit", async function (e) {
    e.preventDefault()
    const name = document.getElementById('name').value;
    const phoneNumber = document.getElementById('contactPhoneNumber').value;
    const email = document.getElementById('email').value;
    const message = document.getElementById('message').value;
    let encryptedEmail = await encryptData(email);
    let encryptedPhone;
    if (phoneNumber) {
        encryptedPhone = await encryptData(phoneNumber);
    }
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');

    const formData = new FormData();

    formData.append('name', name);
    formData.append('email', encryptedEmail);
    if (phoneNumber) {
        formData.append('phoneNumber', encryptedPhone);
    }
    formData.append('message', message);

    try {
        const response = await fetch(this.action, {
            method: 'POST',
            body: formData,
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
            }
        });
        const responseData = await response.json();

        if (responseData.success) {
            if (responseData.redirectUrl) {
                sessionStorage.setItem('successContactRequestMessage', "Your request was successful! Thank you.");
                window.location.href = responseData.redirectUrl;
                // document.getElementById('success').textContent = "Contact request sent successfully."
            }
        } else {
            if (responseData.errors) {
                displayErrors(responseData.errors);
            }
        }
    } catch (error) {
        document.getElementById('error-message').textContent = 'An error occurred. Please try again.';
    }

})