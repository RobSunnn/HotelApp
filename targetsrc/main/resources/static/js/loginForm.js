document.getElementById('login-form').addEventListener('submit', async function (e) {
    e.preventDefault(); // Prevent the default form submission

    // Get email and password from the form
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    let encryptedEmail = await encryptData(email);
    let encryptedPassword = await encryptData(password);
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');

    const formData = new FormData();
    formData.append('encryptedEmail', encryptedEmail);
    formData.append('encryptedPass', encryptedPassword);

    let action = this.action;
    await sendData(action, formData, csrfTokenElement);
});