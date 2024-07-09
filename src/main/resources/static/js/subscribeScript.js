document.getElementById("subscribe-form").addEventListener("submit", async function (e) {
    e.preventDefault();
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');
    const subscriberEmail = document.getElementById('subscriberEmail').value;
    console.log(subscriberEmail)

    let encryptedEmail = await encryptData(subscriberEmail);
    const formData = new FormData();
    formData.append('subscriberEmail', encryptedEmail);

    let action = this.action;
    await sendData(action, formData, csrfTokenElement);
})

document.addEventListener('DOMContentLoaded', (event) => {
    const successMessage = sessionStorage.getItem('subscribeSuccess');
    if (successMessage) {
        const messageElement = document.getElementById('successSubscribeMessage');
        messageElement.querySelector('small').textContent = successMessage;
        messageElement.style.display = 'block';
        messageElement.scrollIntoView({behavior: 'smooth', block: 'center'});
        sessionStorage.removeItem('subscribeSuccess');
    }
});