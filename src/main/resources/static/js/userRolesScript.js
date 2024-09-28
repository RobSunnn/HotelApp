const baseUrl = '`${window.location.origin}/admin`';
document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById('searchInput');
    const tableBody = document.querySelector('#table tbody');
    const paginationDiv = document.getElementById('pagination');
    const pageSize = 7;
    let currentPage = 1;
    let allUsers = [];

    // Fetch all users initially from server
    fetch(baseUrl + "/allUsersFetch")
        .then(response => response.json())
        .then(result => {
            allUsers = result;
            filterAndRender(); // Initial render after fetching users
        })
        .catch(error => console.error('Error fetching users:', error));

    // Function to render users based on search term and pagination
    function renderUsers(users, page = 1) {
        // Clear existing rows in the table body
        tableBody.innerHTML = '';

        // Calculate the range of users to display for the current page
        const start = (page - 1) * pageSize;
        const end = start + pageSize;
        const paginatedUsers = users.slice(start, end);

        // Function to create and return a table cell with content
        function createTableCell(content, className = '') {
            const td = document.createElement('td');
            td.textContent = content;
            if (className) {
                td.className = className;
            }
            return td;
        }

        // Function to create a button with specific attributes
        function createButton(id, text, className, disabled = false) {
            const button = document.createElement('button');
            button.type = 'submit';
            button.name = 'command';
            button.id = id;
            button.className = className;
            button.textContent = text;
            if (disabled) {
                button.setAttribute('disabled', '');
            }
            return button;
        }

        // Create table rows for each user
        paginatedUsers.forEach(user => {
            const row = document.createElement('tr');
            row.setAttribute('data-encrypted', user.encryptedEmail);

            // User Picture
            const userPictureTd = document.createElement('td');
            const img = document.createElement('img');
            img.className = 'profile-picture';
            img.src = user.profilePictureBase64 ? 'data:image/jpeg;base64,' + user.profilePictureBase64 : '/images/profile.jpg';
            img.alt = 'Profile Picture';
            userPictureTd.appendChild(img);

            // User Details
            row.appendChild(userPictureTd);
            row.appendChild(createTableCell(user.fullName));
            row.appendChild(createTableCell(user.email));
            row.appendChild(createTableCell(user.roleNames, 'roles'));

            // Role Buttons
            const functionsTd = document.createElement('td');
            const buttonsHolderDiv = document.createElement('div');
            buttonsHolderDiv.className = 'buttons-holder';

            const form = document.createElement('form');
            form.className = 'buttons-holder-form';

            const makeAdminButton = createButton('makeAdmin', 'Admin', 'role-button btn btn-sm btn-success', user.roleNames.includes("ADMIN"));
            const makeModeratorButton = createButton('makeModerator', 'Moderator', 'role-button btn btn-sm btn-warning', user.roleNames.includes("MODERATOR") && !user.roleNames.includes("ADMIN"));
            const makeUserButton = createButton('makeUser', 'User', 'role-button btn btn-sm btn-danger', user.roleNames.includes("USER") && !user.roleNames.includes("MODERATOR") && !user.roleNames.includes("ADMIN"));

            // Add buttons to form
            form.appendChild(makeAdminButton);
            form.appendChild(makeModeratorButton);
            form.appendChild(makeUserButton);

            // Add form to button holder
            buttonsHolderDiv.appendChild(form);
            functionsTd.appendChild(buttonsHolderDiv);

            // Add result div
            const resultDiv = document.createElement('div');
            resultDiv.id = 'result';
            resultDiv.className = 'result mt-3';
            functionsTd.appendChild(resultDiv);

            // Add function td to row
            row.appendChild(functionsTd);

            // Append row to table body
            tableBody.appendChild(row);

            // Set up button event handlers
            makeAdminButton.addEventListener('click', function (e) {
                e.preventDefault();
                updateButtonStates('ADMIN');
            });
            makeModeratorButton.addEventListener('click', function (e) {
                e.preventDefault();
                updateButtonStates('MODERATOR');
            });
            makeUserButton.addEventListener('click', function (e) {
                e.preventDefault();
                updateButtonStates('USER');
            });

            function updateButtonStates(role) {
                makeAdminButton.disabled = role === 'ADMIN';
                makeModeratorButton.disabled = role === 'MODERATOR';
                makeUserButton.disabled = role === 'USER';
            }
        });

        // Render pagination controls
        renderPagination(users.length, page);
    }


    // Function to render pagination
    function renderPagination(totalItems, currentPage) {
        paginationDiv.innerHTML = '';
        const totalPages = Math.ceil(totalItems / pageSize);

        for (let i = 1; i <= totalPages; i++) {
            const pageItem = document.createElement('div');
            pageItem.className = 'rounded bg-dark page-item' + (i === currentPage ? ' active' : '');
            const pageButton = document.createElement('button');
            pageButton.className = 'page-link';
            pageButton.textContent = i;
            pageButton.addEventListener('click', () => {
                currentPage = i;
                renderUsers(filteredUsers, currentPage);
            });
            pageItem.appendChild(pageButton);
            paginationDiv.appendChild(pageItem);
        }
    }

    // Function to filter users and render based on search term and current page
    let filteredUsers = [];

    function filterAndRender() {
        const searchTerm = searchInput.value.trim().toLowerCase();
        filteredUsers = allUsers.filter(user =>
            user.fullName.toLowerCase().includes(searchTerm) ||
            user.email.toLowerCase().includes(searchTerm)
        );
        currentPage = 1; // Reset to first page on search
        renderUsers(filteredUsers, currentPage);
    }

    // Search input listener
    searchInput.addEventListener('input', filterAndRender);
});

document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.querySelector('#table tbody');
    const resultElements = document.getElementsByClassName('result');

    tableBody.addEventListener('click', (e) => {
        if (e.target && e.target.matches('.role-button')) {
            e.preventDefault();

            const command = e.target.textContent.trim();
            console.log(command)
            const encrypted = e.target.closest('tr').dataset.encrypted;

            let message;
            switch (command) {
                case "Admin":
                    message = 'User has been granted admin rights!';
                    break;
                case "Moderator":
                    message = 'User has been granted moderator rights!';
                    break;
                case "User":
                    message = 'User rights have been revoked!';
                    break;
                default:
                    console.log('Unknown command:', command);
                    return;
            }

            Array.from(resultElements).forEach(result => result.innerHTML = '');

            if (encrypted) {
                const csrfTokenElement = document.querySelector('input[name="_csrf"]');
                const userObj = {
                    encrypted: encrypted,
                    command: command
                };

                fetch(`${baseUrl}/secret`, {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN': csrfTokenElement.value,
                        'Content-type': 'application/json'
                    },
                    body: JSON.stringify(userObj)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! Status: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(responseData => {

                        let htmlParagraphElement = document.createElement('p');
                        htmlParagraphElement.textContent = message;
                        const result = e.target.closest('td').querySelector('.result');
                        result.appendChild(htmlParagraphElement);

                        reloadRoles(encrypted)
                            .then(roleNames => {
                                // Update roles field in the table
                                const rolesField = e.target.closest('tr').querySelector('.roles');
                                rolesField.innerText = '';
                                rolesField.textContent = roleNames;
                            })
                            .catch(err => {
                                console.log('Error reloading roles:', err);
                            });

                    })
                    .catch(error => {
                        console.error('Error:', error);
                        // Handle network or server errors
                    });
            } else {
                console.log('Encrypted email not found.');
            }
        }
    });
});


function reloadRoles(encrypted) {
    return fetch(`${baseUrl}/info?encrypted=${encodeURIComponent(encrypted)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user roles');
            }
            return response.text();
        })
        .then(result => {
            return result;
        })
        .catch(err => {
            console.error('Error reloading roles:', err);
            throw err;
        });

}
