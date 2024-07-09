const baseUrl = 'http://localhost:8080/admin';

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
