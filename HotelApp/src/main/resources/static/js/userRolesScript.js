
const baseUrl = 'http://localhost:8080/admin';

document.addEventListener('DOMContentLoaded', () => {
    const buttonsArr = document.querySelectorAll('.role-button');
    const resultElements = document.getElementsByClassName('result');
    buttonsArr.forEach(b => b.addEventListener('click', (e) => {
        const command = e.target.textContent;
        Array.from(resultElements).forEach(result => result.innerHTML = '');
        const userEmail = e.target.closest('tr').dataset.userEmail;

        let urlForPostRequest;
        let message;

        switch(command) {
            case "Make Admin":
                urlForPostRequest = 'makeUserAdmin';
                message = 'User has been granted admin rights!';
                break;
            case "Make Moderator":
                urlForPostRequest = 'makeUserModerator';
                message = 'User has been granted moderator rights!';
                break;
            case "Make User":
                urlForPostRequest = 'takeRights';
                message = 'User rights have been revoked!';
                break;
        }

        const result = e.target.closest('td').querySelector('.result');
        result.innerHTML = '';
        if (userEmail) {
            const csrfTokenElement = document.querySelector('input[name="_csrf"]');
            const userObj = {
                userEmail: userEmail
            };

            fetch(`${baseUrl}/${urlForPostRequest}/${userEmail}`, {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': csrfTokenElement.value,
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(userObj)
            })
                .then(() => {
                    let htmlParagraphElement = document.createElement('p');
                    htmlParagraphElement.textContent = message;
                    result.appendChild(htmlParagraphElement);

                    reloadRoles(userEmail)
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
                .catch(err => {
                    console.log('Error:', err);
                    // Handle error
                });
        } else {
            console.log('User ID not found');
        }
    }));
});

function reloadRoles(userEmail) {
    return fetch(`${baseUrl}/${userEmail}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user roles');
            }
            return response.json();
        })
        .then(result => {
            return result.roleNames;
        });
}