document.addEventListener("DOMContentLoaded", function () {
    let rows = document.querySelectorAll("#table tbody tr");
    let fullMessageElement = document.getElementById("fullMessage");

    function selectRow(row) {
        // Remove 'selected' class from any previously selected row
        let previouslySelected = document.querySelector("#table tr.selected");
        if (previouslySelected) {
            previouslySelected.classList.remove("selected");
        }
        fullMessageElement.innerText = '';

        // Add 'selected' class to the clicked row
        row.classList.add("selected");

        const authorName = row.getAttribute("data-content-author");
        const authorEmail = row.getAttribute("data-content-email");
        const authorPhoneNumber = row.getAttribute("data-content-phone");
        const fullMessage = row.getAttribute("data-full-text");

        const authorNameHeading = document.createElement('h5');
        authorNameHeading.textContent = 'Author Name:';
        const authorNameParagraphElement = document.createElement('p');
        authorNameParagraphElement.textContent = authorName;

        const messageHeading = document.createElement('h5');
        messageHeading.textContent = 'Message Content:';
        const messageParagraphElement = document.createElement('p');
        messageParagraphElement.textContent = fullMessage;

        if (authorEmail) {
            const emailHeading = document.createElement('h5');
            emailHeading.textContent = 'Email:';
            const emailParagraphElement = document.createElement('p');
            emailParagraphElement.textContent = authorEmail;

            const phoneNumberHeading = document.createElement('h5');
            phoneNumberHeading.textContent = 'Phone Number:';
            const phoneParagraphElement = document.createElement('p');

            if (authorPhoneNumber) {
                phoneParagraphElement.textContent = authorPhoneNumber;
            } else {
                phoneParagraphElement.textContent = `No phone number provided.`;
            }
            fullMessageElement.appendChild(emailHeading);
            fullMessageElement.appendChild(emailParagraphElement);
            fullMessageElement.appendChild(phoneNumberHeading);
            fullMessageElement.appendChild(phoneParagraphElement);
        }

        fullMessageElement.appendChild(authorNameHeading);
        fullMessageElement.appendChild(authorNameParagraphElement);
        fullMessageElement.appendChild(messageHeading);
        fullMessageElement.appendChild(messageParagraphElement);

        // Retrieve the idOfElement from the selected row
        let idOfElement = row.getAttribute("data-content-id");

        // Update the hidden input fields with the selected idOfElement
        document.getElementById("selectedContentId").value = idOfElement;

        // This is for the comments table
        let notApprovedCommentInput = document.getElementById("notApprovedCommentId");

        if (notApprovedCommentInput) {
            notApprovedCommentInput.value = idOfElement;
        }
    }

    // Add click event listener to each row
    rows.forEach(function (row) {
        row.addEventListener("click", function () {
            selectRow(row);
        });
    });

    // Automatically select the first row when the page loads
    if (rows.length > 0) {
        selectRow(rows[0]);
    }
});
