function showFullComment() {
    const select = document.getElementById('commentDropdown');
    const selectedOption = select.options[select.selectedIndex];
    const selectedOptionId = select.value;

    document.getElementById('fullCommentText').innerText = selectedOption.getAttribute('data-full-text');

    // Set the selected comment ID to the hidden input fields

    document.getElementById("selectedCommentId").value = selectedOptionId;
    document.getElementById('notApprovedCommentId').value = selectedOptionId;
}

// Attach the event listener to the dropdown after the DOM content is loaded
document.addEventListener('DOMContentLoaded', (event) => {
    const select = document.getElementById('commentDropdown');

    if (select) {
        select.addEventListener('change', showFullComment);
    }
});
