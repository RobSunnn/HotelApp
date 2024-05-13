const baseUrl = 'http://localhost:8080/admin';

const usersDropdown = document.getElementById('userSelect');
const userSearchBar = document.getElementById('userSearch');
let result = document.getElementById('result');

const allUsersBtn = document.getElementById('getAllUsersBtn');
const findUserBtn = document.getElementById('userFindBtn');

const makeAdminBtn = document.getElementById('makeAdmin');
const makeModeratorBtn = document.getElementById('makeModerator');
const makeOnlyUserBtn = document.getElementById('makeUser');
const clearBtn = document.getElementById('clearBtn');

let selectedUserId; // Variable to store the selected user ID
let counter = 0;


allUsersBtn.addEventListener('click', () => {

    usersDropdown.innerText = '';
    usersDropdown.removeAttribute("disabled");
    usersDropdown.style.background = 'none';
    usersDropdown.style.color = "black";

    fetch(`${baseUrl}/allUsers`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(res => res.json())
        .then(result => {
            Object.values(result).forEach(e => {

                let htmlOptionElement = document.createElement("option");

                htmlOptionElement.append(`User with email: ${e.email}`);
                htmlOptionElement.value = e.email;

                // Generate a unique ID for each user and set it as the ID of the option element
                const dynamicId = 'email_' + counter++;
                selectedUserId = dynamicId;
                htmlOptionElement.setAttribute('id', dynamicId);

                // Append the option element to the dropdown
                usersDropdown.appendChild(htmlOptionElement);
            });

            document.getElementById('usersDropdown').hidden = false;
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

findUserBtn.addEventListener('click', () => {
    const userEmail = userSearchBar;
    usersDropdown.innerText = '';
    const csrfTokenElement = document.querySelector('input[name="_csrf"]');
    usersDropdown.removeAttribute("disabled");
    usersDropdown.style.background = 'none';
    usersDropdown.style.color = "black";
    findUserBtn.disabled = true;
    usersDropdown.disabled = true;

    fetch(`${baseUrl}/${userEmail.value}`, {
        method: 'GET',
        headers: {
            'X-CSRF-TOKEN': csrfTokenElement.value,
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            return response.json();

        })
        .then(result => {
                if (result === undefined) {
                 let htmlOptionElement = document.createElement("option");
                            htmlOptionElement.append('User with this email does not exist');
                            usersDropdown.appendChild(htmlOptionElement);
                            usersDropdown.style.background = "red";
                            usersDropdown.style.color = "white";
                }
                let htmlOptionElement = document.createElement("option");

                htmlOptionElement.append(`User with email: ${result.email}`);
                htmlOptionElement.value = result.email;

                // Generate a unique ID for the user and set it as the ID of the option element
                const dynamicId = 'email_' + Date.now();
                htmlOptionElement.setAttribute('id', dynamicId);

                // Store the generated ID in the selectedUserId variable
                selectedUserId = dynamicId;

                usersDropdown.appendChild(htmlOptionElement);
                usersDropdown.setAttribute("disabled", '');

        })
        .catch(error => {
            // Handle the case where the user is not found

            let htmlOptionElement = document.createElement("option");
            htmlOptionElement.append('User with this email does not exist');
            usersDropdown.appendChild(htmlOptionElement);
            usersDropdown.style.background = "red";
            usersDropdown.style.color = "white";

        })
        .finally(() => {
            // Enable buttons after completing the request (whether successful or not)
            findUserBtn.disabled = false;
            usersDropdown.disabled = false;
        });

    document.getElementById('usersDropdown').hidden = false;
});

makeAdminBtn.addEventListener('click', () => {
    // Retrieve the selected user's ID using the stored variable
    const selectedUserIdElement = document.getElementById(selectedUserId);
    result.innerText = '';
    // Ensure the selected user's ID is valid
    if (selectedUserIdElement) {
        const userEmail = selectedUserIdElement.value;
        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const emailObj = {
            email: userEmail
        };

        fetch(`${baseUrl}/makeUserAdmin/${userEmail}`, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
                'Content-type': 'application/json'
            },
            body: JSON.stringify(emailObj)
        })
            .then(() => {

                let htmlParagraphElement = document.createElement('p');
                htmlParagraphElement.textContent = 'User have all the rights!'

                result.appendChild(htmlParagraphElement);
            })
            .catch(err => {
                console.log('Error:', err);
                // Handle error
            });
    } else {
        console.log('Selected user ID not found');
    }
});

makeModeratorBtn.addEventListener('click', () => {
    const selectedUserIdElement = document.getElementById(selectedUserId);
    result.innerText = '';

    if (selectedUserIdElement) {
        const userEmail = selectedUserIdElement.value;
        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const emailObj = {
            email: userEmail
        };

        fetch(`${baseUrl}/makeUserModerator/${userEmail}`, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
                'Content-type': 'application/json'
            },
            body: JSON.stringify(emailObj)
        })
            .then(() => {
                let htmlParagraphElement = document.createElement('p');
                htmlParagraphElement.textContent = 'User have the right to be Moderator!'

                result.appendChild(htmlParagraphElement);
            })
            .catch(err => {
                console.log('Error:', err);
                // Handle error
            });

    } else {
        console.log('Selected user ID not found');
    }
})

makeOnlyUserBtn.addEventListener('click', () => {
    const selectedUserIdElement = document.getElementById(selectedUserId);
    result.innerText = '';

    if (selectedUserIdElement) {
        const userEmail = selectedUserIdElement.value;
        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const emailObj = {
            email: userEmail
        };

        fetch(`${baseUrl}/takeRights/${userEmail}`, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
                'Content-type': 'application/json'
            },
            body: JSON.stringify(emailObj)
        })
            .then(() => {
                let htmlParagraphElement = document.createElement('p');
                htmlParagraphElement.textContent = 'You have taken all the rights of the user!'

                result.appendChild(htmlParagraphElement);
            })
            .catch(err => {
                console.log('Error:', err);
                // Handle error
            });

    } else {
        console.log('Selected user ID not found');
    }
});

clearBtn.addEventListener('click', () => {
    location.reload();
})

// Event listener for selecting a user from the dropdown
usersDropdown.addEventListener('change', (event) => {
    // Store the selected user's ID in the variable
    selectedUserId = event.target.selectedOptions[0].id;
});