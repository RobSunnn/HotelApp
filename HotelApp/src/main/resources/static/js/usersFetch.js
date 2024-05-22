const baseUrl = 'http://localhost:8080/admin';

const userSearchBar = document.getElementById('userSearch');
const usersDropdown = document.getElementById('userSelect');
const result = document.getElementById('result');

const allUsersBtn = document.getElementById('getAllUsersBtn');
const findUserBtn = document.getElementById('userFindBtn');

const allButtonsContainer = document.getElementsByClassName('buttons')[0];
const makeAdminBtn = document.getElementById('makeAdmin');
const makeModeratorBtn = document.getElementById('makeModerator');
const makeOnlyUserBtn = document.getElementById('makeUser');
const clearBtn = document.getElementById('clearBtn');

const buttonsArr = [makeAdminBtn, makeModeratorBtn, makeOnlyUserBtn];

let selectedUserId; // Variable to store the selected user ID
let counter = 0;

allUsersBtn.addEventListener('click', () => {
    userSearchBar.style.background = 'none';
    userSearchBar.style.color = 'black';
    userSearchBar.innerText = '';

    usersDropdown.innerText = '';
    usersDropdown.removeAttribute("disabled");
    usersDropdown.style.background = 'none';
    usersDropdown.style.color = "black";

    allButtonsContainer.hidden = false;

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
                userSearchBar.style.color = 'white';
                usersDropdown.style.color = 'white';
                htmlOptionElement.append(`User with email: ${e.email}`);
                htmlOptionElement.value = e.email;
                htmlOptionElement.style.color = 'black';
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
    allButtonsContainer.hidden = true;
    document.getElementById('usersDropdown').hidden = true;

    if (userEmail.value === '') {
        userEmail.style.background = 'red';
        userEmail.setAttribute('placeholder', 'You should enter user email..');
        userEmail.classList.add("white-placeholder");

        userSearchBar.style.color = 'white';
        return;
    }

    usersDropdown.innerText = '';
    usersDropdown.style.background = 'none';
    usersDropdown.style.color = "black";
    usersDropdown.disabled = true;

    const csrfTokenElement = document.querySelector('input[name="_csrf"]');

    fetch(`${baseUrl}/${userEmail.value}`, {
        method: 'GET',
        headers: {
            'X-CSRF-TOKEN': csrfTokenElement.value,
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            if (!response.ok) {
                allButtonsContainer.hidden = true;
                userEmail.style.background = 'red';
                userSearchBar.style.color = "white";
                throw new Error(response.statusText);
            }
            return response.json();

        })
        .then(result => {
            if (result === undefined) {
                userSearchBar.style.background = "blue";
                userSearchBar.style.color = "white";

                return;
            }
            let htmlOptionElement = document.createElement("option");

            htmlOptionElement.append(`User with email: ${result.email}`);
            htmlOptionElement.value = result.email;

            // Generate a unique ID for the user and set it as the ID of the option element
            const dynamicId = 'email_' + Date.now();
            htmlOptionElement.setAttribute('id', dynamicId);

            // Store the generated ID in the selectedUserId variable
            selectedUserId = dynamicId;

            allButtonsContainer.hidden = false;

            usersDropdown.appendChild(htmlOptionElement);
            usersDropdown.setAttribute('disabled', '');

        })
        .catch(() => {
            // Handle the case where the user is not found

            let htmlOptionElement = document.createElement("option");
            htmlOptionElement.append(`User with email: "${userEmail.value}" - does not exist`);
            usersDropdown.appendChild(htmlOptionElement);
            usersDropdown.style.background = "red";
            usersDropdown.style.color = "white";

        });

    document.getElementById('usersDropdown').hidden = false;
});

buttonsArr.forEach(b => b.addEventListener('click', (e) => {
    const command = e.target.textContent;

    let urlForPostRequest;
    let message;

    switch(command) {
        case "Make Admin":
            urlForPostRequest = 'makeUserAdmin';
            message = 'User have all the rights!';
            break;
        case"Make Moderator":
            urlForPostRequest = 'makeUserModerator';
            message = 'User have the right to be Moderator!';
            break;
        case"Make User":
            urlForPostRequest = 'takeRights';
            message = 'You have taken all the rights of the user!';
            break;
    }

    const selectedUserIdElement = document.getElementById(selectedUserId);
    result.innerText = '';

    // Ensure the selected user's ID is valid
    if (selectedUserIdElement) {
        const userEmail = selectedUserIdElement.value;
        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const emailObj = {
            email: userEmail
        };

        fetch(`${baseUrl}/${urlForPostRequest}/${userEmail}`, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
                'Content-type': 'application/json'
            },
            body: JSON.stringify(emailObj)
        })
            .then(() => {

                let htmlParagraphElement = document.createElement('p');
                htmlParagraphElement.textContent = message;

                result.appendChild(htmlParagraphElement);
            })
            .catch(err => {
                console.log('Error:', err);
                // Handle error
            });
    } else {
        console.log('Selected user ID not found');
    }


}))

clearBtn.addEventListener('click', () => {
    location.reload();
})

// Event listener for selecting a user from the dropdown
usersDropdown.addEventListener('change', (event) => {
    // Store the selected user's ID in the variable
    selectedUserId = event.target.selectedOptions[0].id;
});