async function encryptData(value) {
    try {
        // Fetch Base64-encoded public key from server
        const publicKeyBase64 = await fetchPublicKey();
        // Decode Base64-encoded public key
        const publicKeyBytes = forge.util.decode64(publicKeyBase64);
        // Convert to forge RSA public key object
        const publicKeyObj = forge.pki.publicKeyFromAsn1(forge.asn1.fromDer(publicKeyBytes));
        // Encrypt value using RSAES-PKCS1-v1_5
        const encryptedBytes = publicKeyObj.encrypt(value, 'RSAES-PKCS1-V1_5');
        
        // Return encrypted bytes as Base64-encoded string
        return forge.util.encode64(encryptedBytes);
    } catch (error) {
        document.getElementById('error-message').textContent = 'An error occurred. Please try again.';
    }
}

async function fetchPublicKey() {
    try {
        const response = await fetch('http://localhost:8080/get-public-key');
        if (!response.ok) {
            throw new Error('Failed to fetch public key');
        }
        const publicKeyPem = await response.text();
        return publicKeyPem;
    } catch (error) {
        console.error('Error fetching public key:', error);
        throw error; // Propagate the error
    }
}

async function sendData(action, data, csrfTokenElement) {
    try {
        const response = await fetch(action, {
            method: 'POST',
            body: data,
            headers: {
                'X-CSRF-TOKEN': csrfTokenElement.value,
            }
        });

        const responseData = await response.json();

        if (responseData.success) {
            if (responseData.redirectUrl) {
                if (action.includes("changePassword")) {
                    sessionStorage.setItem('successPasswordChange', "Your password is changed successfully!");
                } else if (action.includes("contactForm")) {
                    sessionStorage.setItem('successContactRequestMessage', "Your request was successful! Thank you.");
                } else if (action.includes('subscribe')) {
                    sessionStorage.setItem('subscribeSuccess', "Thank you for subscribing!");
                } else if (action.includes("addComment")) {
                    sessionStorage.setItem("commentSuccess", "Thank you for your comment!");
                }
                window.location.href = responseData.redirectUrl;
            }
        } else {
            if (action.includes("/users/login")) {
                let errorMessage = document.getElementById('error-message');
                errorMessage.textContent = responseData.message;
                errorMessage.classList.add("errors", "alert-danger", "mb-2", "text-center");
            }
            if (responseData.errors) {
                displayErrors(responseData.errors);
            }
        }
    } catch (error) {
        document.getElementById('error-message').textContent = 'An error occurred. Please try again.';
    }
}