document.addEventListener("DOMContentLoaded", function () {
    // Handle "Show More" for individual long comments
    document.querySelectorAll('.showWholeComment').forEach(button => {
        button.addEventListener('click', function () {
            const messageText = this.previousElementSibling;
            messageText.classList.remove('truncated');
            messageText.classList.add('expanded');
            this.style.display = 'none';
        });
    });
});

document.getElementById("comments-form").addEventListener("submit", async function(e) {
    e.preventDefault();

    const author = document.getElementById("author").value;
    const commentContent = document.getElementById("commentContent").value;

    const csrfTokenElement = document.querySelector('input[name="_csrf"]');

    const formData = new FormData();
    formData.append('author', author);
    formData.append('commentContent', commentContent);

    let action = this.action;
    await sendData(action, formData, csrfTokenElement);
})

document.addEventListener('DOMContentLoaded', (event) => {
    const successMessage = sessionStorage.getItem('commentSuccess');
    if (successMessage) {
        const messageElement = document.getElementById('successMessage');
        messageElement.querySelector('small').textContent = successMessage;
        messageElement.style.display = 'block';
        messageElement.scrollIntoView({behavior: 'smooth', block: 'center'});
        sessionStorage.removeItem('commentSuccess');
    }
});