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
        // const encryptedBytes = publicKeyObj.encrypt(value, 'RSA-OAEP', {
        //     md: forge.md.sha256.create(),
        //     mgf1: {
        //         md: forge.md.sha1.create()  // Default MGF1 digest is SHA-1, ensure this matches the Java setup
        //     }
        // });
        // Return encrypted bytes as Base64-encoded string
        return forge.util.encode64(encryptedBytes);
    } catch (error) {
        console.error("Encryption error:", error);
        throw error; // Handle or throw an exception as appropriate
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