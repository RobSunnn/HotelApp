const baseUrl = 'http://localhost:8080/admin/allUsers';

const usersDropdown = document.getElementById('userSelect');
const userSearchBar = document.getElementById('userSearch');
const findUserBtn = document.getElementById('userFindBtn');


usersDropdown.addEventListener('click', () => {
    fetch(baseUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(res => res.json())
        .then(result => {
            usersDropdown.innerText = "";

            Object.values(result).forEach(e => {
                let htmlOptionElement = document.createElement("option");

                htmlOptionElement.append(`User with email: ${e.email}`);
                htmlOptionElement.value = e.email;
                usersDropdown.appendChild(htmlOptionElement);
            });

        })
        .catch(error => {
            console.error('Error:', error);
        });
});

usersDropdown.addEventListener('change', () => {
    usersDropdown.removeEventListener('click',ev => console.log(ev));
})

findUserBtn.addEventListener('click', () => {
    const userEmail = userSearchBar.value;

    fetch(`${baseUrl}/${userEmail}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(res => res.json())
        .then(res => {
            usersDropdown.innerText = '';

            let htmlOptionElement = document.createElement("option");

            htmlOptionElement.append(`User with email: ${res.email}`);
            htmlOptionElement.value = res.email;

            usersDropdown.appendChild(htmlOptionElement);
            usersDropdown.setAttribute("disabled", '');

            let titleOfDropdown = document.getElementById('titleDropdown');
            titleOfDropdown.style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
