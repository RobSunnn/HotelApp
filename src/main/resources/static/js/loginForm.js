document.getElementById('login-form').addEventListener('submit', async function (e) {
    e.preventDefault(); // Prevent the default form submission

    const key = CryptoJS.lib.WordArray.random(32); // 256-bit key
    const iv = CryptoJS.lib.WordArray.random(16);
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const encryptedEmail = CryptoJS.AES.encrypt(email, key, {iv: iv}).toString();
    const encryptedPassword = CryptoJS.AES.encrypt(password, key, {iv: iv}).toString();

    const formData = new FormData();
    formData.append('encryptedEmail', encryptedEmail);
    formData.append('encryptedPass', encryptedPassword);
    formData.append('iv', CryptoJS.enc.Base64.stringify(iv));
    formData.append('key', CryptoJS.enc.Base64.stringify(key));
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