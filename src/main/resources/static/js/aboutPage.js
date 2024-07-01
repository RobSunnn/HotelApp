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