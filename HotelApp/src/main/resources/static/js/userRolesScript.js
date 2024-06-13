const baseUrl = 'http://localhost:8080/admin';

document.addEventListener('DOMContentLoaded', () => {
    const buttonsArr = document.querySelectorAll('.role-button');
    const resultElements = document.getElementsByClassName('result');
    buttonsArr.forEach(b => b.addEventListener('click', (e) => {
        const command = e.target.textContent;
        Array.from(resultElements).forEach(result => result.innerHTML = '');
        const encrypted = e.target.closest('tr').dataset.encrypted;

        let message;

        switch(command) {
            case "Make Admin":
                message = 'User has been granted admin rights!';
                break;
            case "Make Moderator":
                message = 'User has been granted moderator rights!';
                break;
            case "Make User":
                message = 'User rights have been revoked!';
                break;
        }

        const result = e.target.closest('td').querySelector('.result');
        result.innerHTML = '';
        if (encrypted) {
            const csrfTokenElement = document.querySelector('input[name="_csrf"]');
            const userObj = {
                encrypted: encrypted,
                command: command
            };

            fetch(`${baseUrl}/changeUserRoles`, {
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
                .catch(err => {
                    console.log('Error:', err);
                    // Handle error
                });
        } else {
            console.log('User ID not found');
        }
    }));
});

function reloadRoles(encrypted) {
    return fetch(`${baseUrl}/info?encrypted=${encodeURIComponent(encrypted)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user roles');
            }
            return response.json();
        })
        .then(result => {
            console.log('User roles:', result);
            return result.roleNames;
        })
        .catch(err => {
            console.error('Error reloading roles:', err);
            throw err;
        });
}
