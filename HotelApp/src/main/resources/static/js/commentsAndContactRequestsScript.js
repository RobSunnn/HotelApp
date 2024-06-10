document.addEventListener("DOMContentLoaded", function() {
    let rows = document.querySelectorAll("#commentTable tbody tr");

    function selectRow(row) {
        // Remove 'selected' class from any previously selected row
        let previouslySelected = document.querySelector("#commentTable tr.selected");
        if (previouslySelected) {
            previouslySelected.classList.remove("selected");
        }

        // Add 'selected' class to the clicked row
        row.classList.add("selected");

        let fullText = row.getAttribute("data-full-text");
        document.getElementById("fullMessage").innerText = fullText;

        // Retrieve the commentId from the selected row
        let commentId = row.getAttribute("data-content-id");

        // Update the hidden input fields with the selected commentId
        document.getElementById("selectedContentId").value = commentId;
        let notApprovedCommentInput = document.getElementById("notApprovedCommentId");

        if (notApprovedCommentInput) {
            notApprovedCommentInput.value = commentId;
        }
    }

    // Add click event listener to each row
    rows.forEach(function(row) {
        row.addEventListener("click", function() {
            selectRow(row);
        });
    });

    // Automatically select the first row when the page loads
    if (rows.length > 0) {
        selectRow(rows[0]);
    }
});
