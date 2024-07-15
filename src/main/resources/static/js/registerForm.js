document.getElementById("register-form").addEventListener('submit', async function (e) {
    e.preventDefault();
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

    let action = this.action;
    await sendData(action, formData, csrfTokenElement);
});

