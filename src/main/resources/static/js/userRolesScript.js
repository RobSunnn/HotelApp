const baseUrl = 'http://localhost:8080/admin';
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
        tableBody.innerHTML = ''; // Clear existing table rows

        const start = (page - 1) * pageSize;
        const end = start + pageSize;
        const paginatedUsers = users.slice(start, end);

        paginatedUsers.forEach(user => {
            const row = document.createElement('tr');
            row.setAttribute('data-encrypted', user.encryptedEmail);

            const userPictureTd = document.createElement('td');
            userPictureTd.style.width = '10vw';
            const img = document.createElement('img');
            img.className = 'profile-picture';
            img.src = user.profilePictureBase64 ? 'data:image/jpeg;base64,' + user.profilePictureBase64 : '/images/minion.gif';
            img.alt = 'Profile Picture';
            userPictureTd.appendChild(img);

            const fullNameTd = document.createElement('td');
            fullNameTd.style.width = '10vw';
            fullNameTd.textContent = user.fullName;

            const emailTd = document.createElement('td');
            emailTd.style.width = '10vw';
            emailTd.textContent = user.email;

            const rolesTd = document.createElement('td');
            rolesTd.style.width = '10vw';
            rolesTd.textContent = user.roleNames;
            rolesTd.className = 'roles';

            const functionsTd = document.createElement('td');
            functionsTd.style.width = '40vw';
            const buttonsHolderDiv = document.createElement('div');
            buttonsHolderDiv.className = 'buttons-holder';

            const form = document.createElement('form');

            const makeAdminButton = document.createElement('button');
            makeAdminButton.type = 'submit';
            makeAdminButton.name = 'command';
            makeAdminButton.className = 'role-button btn btn-success';
            makeAdminButton.id = 'makeAdmin';
            makeAdminButton.textContent = 'Make Admin';

            const makeModeratorButton = document.createElement('button');
            makeModeratorButton.type = 'submit';
            makeModeratorButton.name = 'command';
            makeModeratorButton.className = 'role-button btn btn-warning';
            makeModeratorButton.id = 'makeModerator';
            makeModeratorButton.textContent = 'Make Moderator';

            const makeUserButton = document.createElement('button');
            makeUserButton.type = 'submit';
            makeUserButton.name = 'command';
            makeUserButton.className = 'role-button btn btn-danger';
            makeUserButton.id = 'makeUser';
            makeUserButton.textContent = 'Make User';

            form.appendChild(makeAdminButton);
            form.appendChild(makeModeratorButton);
            form.appendChild(makeUserButton);

            if (user.roleNames.includes("ADMIN")) {
                makeAdminButton.setAttribute("disabled", "")
            }
            makeAdminButton.addEventListener('click', function (e) {
                e.preventDefault();
                handleButtonClick(user.encryptedEmail, 'admin');
                makeAdminButton.setAttribute("disabled", "");
                makeModeratorButton.removeAttribute("disabled");
                makeUserButton.removeAttribute("disabled");
            });

            if (user.roleNames.includes("MODERATOR") && !user.roleNames.includes("ADMIN")) {
                makeModeratorButton.setAttribute("disabled", "")
            }
            makeModeratorButton.addEventListener('click', function (e) {
                e.preventDefault();
                handleButtonClick(user.encryptedEmail, 'moderator');
                makeModeratorButton.setAttribute("disabled", "");
                makeAdminButton.removeAttribute("disabled");
                makeUserButton.removeAttribute("disabled");
            });

            if (user.roleNames.includes("USER") && !user.roleNames.includes("MODERATOR") && !user.roleNames.includes("ADMIN")) {
                makeUserButton.setAttribute("disabled", "")
            }
            makeUserButton.addEventListener('click', function (e) {
                e.preventDefault();
                handleButtonClick(user.encryptedEmail, 'user');
                makeAdminButton.removeAttribute("disabled");
                makeModeratorButton.removeAttribute("disabled");
                makeUserButton.setAttribute("disabled", "")
            });

            buttonsHolderDiv.appendChild(form);
            functionsTd.appendChild(buttonsHolderDiv);

            const resultDiv = document.createElement('div');
            resultDiv.id = 'result';
            resultDiv.className = 'result mt-3';
            functionsTd.appendChild(resultDiv);

            row.appendChild(userPictureTd);
            row.appendChild(fullNameTd);
            row.appendChild(emailTd);
            row.appendChild(rolesTd);
            row.appendChild(functionsTd);

            tableBody.appendChild(row);
        });

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

    // Function to handle button clicks
    function handleButtonClick(encryptedEmail, role) {
        console.log('Button clicked for encrypted email:', encryptedEmail, 'Role:', role);
        // Here you can perform actions with the encrypted email and role, like making API requests
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.querySelector('#table tbody');
    const resultElements = document.getElementsByClassName('result');

    tableBody.addEventListener('click', (e) => {
        if (e.target && e.target.matches('.role-button')) {
            e.preventDefault();

            const command = e.target.textContent.trim();
            const encrypted = e.target.closest('tr').dataset.encrypted;

            let message;
            switch (command) {
                case "Make Admin":
                    message = 'User has been granted admin rights!';
                    break;
                case "Make Moderator":
                    message = 'User has been granted moderator rights!';
                    break;
                case "Make User":
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
            console.log('User roles:', result);
            return result;
        })
        .catch(err => {
            console.error('Error reloading roles:', err);
            throw err;
        });

}
