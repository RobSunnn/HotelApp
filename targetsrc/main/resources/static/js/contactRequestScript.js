document.getElementById("contact-form").addEventListener("submit", async function (e) {
    e.preventDefault();
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

    let action = this.action;
    await sendData(action, formData, csrfTokenElement);
});

document.addEventListener('DOMContentLoaded', (event) => {
    const successMessage = sessionStorage.getItem('successContactRequestMessage');
    if (successMessage) {
        const messageElement = document.getElementById('successMessage');
        messageElement.querySelector('small').textContent = successMessage;
        messageElement.style.display = 'block';
        messageElement.scrollIntoView({behavior: 'smooth', block: 'center'});
        sessionStorage.removeItem('successContactRequestMessage');
    }
});