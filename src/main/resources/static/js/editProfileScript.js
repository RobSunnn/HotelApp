const userToken = /*[[${userToken}]]*/ 'userToken';

document.addEventListener('DOMContentLoaded', function () {
    let userPicture = document.getElementById('profile-picture-image');
    if (userPicture) {
        fetch(`/users/profile/details?token=${userToken}`)
            .then(response => response.json())
            .then(userDetails => {
                userPicture.src = userDetails.profilePictureBase64 ? 'data:image/jpeg;base64,' + userDetails.profilePictureBase64 : '/images/profile.jpg';
                document.getElementById('fullName').innerText = userDetails.fullName;
                document.getElementById('age').innerText = userDetails.age + ' years';
                document.getElementById('email').innerText = userDetails.email;

                const rolesContainer = document.getElementById('roles');
                userDetails.roles.forEach(role => {
                    const roleElement = document.createElement('span');
                    roleElement.className = 'bg-info p-1 m-1 px-4 rounded text-white';
                    roleElement.innerText = role.name;
                    rolesContainer.appendChild(roleElement);
                });
            }).catch(error => {
            console.log('Error fetching user details:', error);
        });
    }
});

const editProfileForm = document.getElementById("edit-profile-form");
if (editProfileForm) {
    document.addEventListener('DOMContentLoaded', function () {
        fetch(`/users/profile/details?token=${userToken}`)
            .then(response => response.json())
            .then(userDetails => {
                document.getElementById('firstName').value = userDetails.firstName;
                document.getElementById('lastName').value = userDetails.lastName;
                document.getElementById('email').value = userDetails.email;
                document.getElementById('age').value = userDetails.age;
            })
            .catch(error => {
                console.log('Error fetching user details:', error);
            });
    });

    editProfileForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const email = document.getElementById('email').value;
        const age = document.getElementById('age').value;

        let encryptedEmail = await encryptData(email);

        const formData = new FormData();
        formData.append('firstName', firstName);
        formData.append('lastName', lastName);
        formData.append('email', encryptedEmail);
        formData.append('age', age);

        let action = this.action;
        await sendData(action, formData, csrfTokenElement);
    });
}

let changePasswordForm = document.getElementById('change-password-form');
if (changePasswordForm) {
    changePasswordForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const csrfTokenElement = document.querySelector('input[name="_csrf"]');
        const oldPassword = document.getElementById('oldPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmNewPassword = document.getElementById('confirmNewPassword').value;

        let encryptedOldPassword = await encryptData(oldPassword);
        let encryptedNewPassword = await encryptData(newPassword);
        let encryptedConfirmNewPassword = await encryptData(confirmNewPassword);
        console.log(encryptedOldPassword)

        const formData = new FormData();
        formData.append('oldPassword', encryptedOldPassword);
        formData.append('newPassword', encryptedNewPassword);
        formData.append('confirmNewPassword', encryptedConfirmNewPassword);

        let action = this.action;
        await sendData(action, formData, csrfTokenElement);
    });
}

document.addEventListener('DOMContentLoaded', (event) => {
    const successMessage = sessionStorage.getItem('successPasswordChange');
    if (successMessage) {
        const messageElement = document.getElementById('successMessage');
        messageElement.querySelector('small').textContent = successMessage;
        messageElement.style.display = 'block';
        sessionStorage.removeItem('successPasswordChange');
    }
});