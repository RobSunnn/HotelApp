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

    try {
        const response = await fetch(this.action, {
            method: 'POST',
            body: formData,
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
            }
        });
        if (response.ok) {
            window.location.href = '/';
        } else {
            const result = await response.json();
            let errorMessage = document.getElementById('error-message');
            errorMessage.textContent = result.message;
            errorMessage.classList.add("errors", "alert-danger", "mb-2", "text-center");
        }
    } catch (error) {
        document.getElementById('error-message').textContent = 'An error occurred. Please try again.';
    }
});