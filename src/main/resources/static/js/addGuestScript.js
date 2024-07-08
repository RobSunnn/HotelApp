document.getElementById("add-guest-form").addEventListener("submit", async function (e) {
    e.preventDefault();
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const age = document.getElementById('age').value;
    const documentId = document.getElementById('documentId').value;
    const daysToStay = document.getElementById('daysToStay').value;
    const roomNumber = document.getElementById('roomNumber').value;

    let encryptedEmail = await encryptData(email);
    let encryptedDocumentId = await encryptData(documentId);
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');

    const formData = new FormData();
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('email', encryptedEmail);
    formData.append('age', age);
    formData.append('documentId', encryptedDocumentId);
    formData.append('daysToStay', daysToStay);
    formData.append('roomNumber', roomNumber);

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