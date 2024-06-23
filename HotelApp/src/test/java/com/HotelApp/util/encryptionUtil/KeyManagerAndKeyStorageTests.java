package com.HotelApp.util.encryptionUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeyManagerAndKeyStorageTests {

    private static final String ORIGINAL_DATA = "Integration Test Data";

    @BeforeEach
    void setUp() throws IOException {
        deleteSecretKeyFile();
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteSecretKeyFile();
    }

    @Test
    void testEncryptDecryptIntegration() throws Exception {
        KeyManager.regenerateKey();

        String encryptedData = EncryptionUtil.encrypt(ORIGINAL_DATA);
        String decryptedData = EncryptionUtil.decrypt(encryptedData);

        assertEquals(ORIGINAL_DATA, decryptedData);
    }

    @Test
    void testEncryptDecryptWithExternalKeyIntegration() throws Exception {
        KeyManager.regenerateKey();
        SecretKey secretKey = KeyManager.getSecretKey();

        String encryptedData = EncryptionUtil.encrypt(ORIGINAL_DATA, secretKey);
        String decryptedData = EncryptionUtil.decrypt(encryptedData, secretKey);

        assertEquals(ORIGINAL_DATA, decryptedData);
    }

    @Test
    void testRetrieveKey() throws Exception {
        // Generate a secret key and store it
        KeyManager.regenerateKey();

        // Retrieve the stored key
        String storedKey = KeyStorageUtil.retrieveKey();

        // Get the secret key directly from KeyManager to compare
        SecretKey secretKey = KeyManager.getSecretKey();
        String expectedKey = EncryptionUtil.keyToString(secretKey);

        // Assert that the retrieved key matches the expected key
        assertEquals(expectedKey, storedKey);
    }

    @Test
    void testRetrieveKeyWhenExpired() {
        // In this test, we don't regenerate the key, so it will be expired
        // Attempt to retrieve the key should throw a RuntimeException
        assertThrows(NoSuchFileException.class, KeyStorageUtil::retrieveKey);
    }

    private void deleteSecretKeyFile() throws IOException {
        if (Files.exists(Paths.get("secret.key"))) {
            Files.delete(Paths.get("secret.key"));
        }
    }
}
