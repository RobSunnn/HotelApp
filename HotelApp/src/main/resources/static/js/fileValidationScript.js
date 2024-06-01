// Attach validation function to form submit event
document.getElementById('profile-form').addEventListener('submit', function(event) {
    event.preventDefault();

    let isValid = validateFile();

    if (isValid) {
        this.submit();
    }
});

function validateFile() {
    let fileInput = document.getElementById('profile-picture');
    let fileSize = fileInput.files[0].size;
    let maxSize = 10 * 1024 * 1024;

    if (fileSize > maxSize) {
        alert('File size is too big!\nYou need to changed it with a smaller image.\nBest Regards!');
        return false;
    }
    return true;
}
