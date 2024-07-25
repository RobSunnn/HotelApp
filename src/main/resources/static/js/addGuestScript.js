document.getElementById("add-guest-form").addEventListener("submit", async function (e) {
    e.preventDefault();
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const age = document.getElementById('age').value;
    const documentId = document.getElementById('documentId').value;
    const daysToStay = document.getElementById('daysToStay').value;
    const roomNumber = document.getElementById('roomNumber').value;
    const roomNumberError = document.getElementById('roomNumberError');
    if (!roomNumber || isNaN(Number(roomNumber))) {
        // Display error message
        roomNumberError.textContent = 'Please select a valid room number.';
        roomDropdown.classList.add('is-invalid');
        return; // Prevent form submission
    }

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

    let action = this.action;
    await sendData(action, formData, csrfTokenElement);

})