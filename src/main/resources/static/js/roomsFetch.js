const baseUrl = '/api/rooms/';
const roomDropdown = document.getElementById("roomNumber");

// Get all the checkboxes
const checkboxes = document.querySelectorAll('input[type="checkbox"]');
// Add event listener to each checkbox
checkboxes.forEach(checkbox => {

    checkbox.addEventListener('change', function () {

        // Uncheck all checkboxes except the one that was just clicked
        checkboxes.forEach(cb => {
            if (cb !== checkbox) {
                cb.checked = false;
            }

        });

        if (!checkbox.checked) {
            roomDropdown.innerText = "";
            const defaultOption = document.createElement("option");
            defaultOption.textContent = "Here you must select a free room after you select category.";
            defaultOption.disabled = true;
            defaultOption.selected = true; // Select the default option by default
            roomDropdown.appendChild(defaultOption);
        } else {
            const value = checkbox.value;
            const index = checkbox.getAttribute('data-index');

            // Send AJAX request
            fetch(`${baseUrl}${value}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },

            })
                .then(response => response.json())
                .then(data => {

                    roomDropdown.innerText = "";

                    if (Object.keys(data).length === 0) {
                        // Handle case where data is empty
                        const defaultOption = document.createElement("option");
                        defaultOption.textContent = "No free rooms of this category available.";
                        defaultOption.disabled = true;
                        defaultOption.selected = true; // Select the default option by default
                        roomDropdown.appendChild(defaultOption);

                    } else {

                        let htmlFirstOptionElement = document.createElement("option");
                        htmlFirstOptionElement.textContent = 'Select Room';
                        htmlFirstOptionElement.disabled = true;
                        htmlFirstOptionElement.selected = true;

                        roomDropdown.appendChild(htmlFirstOptionElement);

                        Object.values(data).forEach(e => {

                            let htmlOptionElement = document.createElement("option");
                            htmlOptionElement.append(`Room number: ${e.roomNumber}`);
                            htmlOptionElement.value = e.roomNumber;

                            roomDropdown.appendChild(htmlOptionElement);
                        });
                    }

                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    });
});

