document.addEventListener('DOMContentLoaded', function() {
    const userToken = /*[[${userToken}]]*/ 'userTokenPlaceholder';

    fetch(`/users/profile/details?token=${userToken}`)
        .then(response => response.json())
        .then(userDetails => {
            document.getElementById('firstName').value = userDetails.firstName;
            document.getElementById('lastName').value = userDetails.lastName;
            document.getElementById('email').value = userDetails.email;
            document.getElementById('age').value = userDetails.age;
        })
        .catch(error => {
            console.error('Error fetching user details:', error);
        });
});

document.getElementById("edit-profile-form").addEventListener("submit", async function (e) {
    e.preventDefault();
    const key = CryptoJS.lib.WordArray.random(32);
    const iv = CryptoJS.lib.WordArray.random(16);
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const age = document.getElementById('age').value;


    const encryptedEmail = CryptoJS.AES.encrypt(email, key, {iv: iv}).toString();

    const formData = new FormData();
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('email', encryptedEmail);
    formData.append('age', age);
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

        const responseData = await response.json();


        if (responseData.success) {
            if (responseData.redirectUrl) {
                window.location.href = responseData.redirectUrl;
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