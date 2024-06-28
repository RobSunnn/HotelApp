package com.HotelApp.util.encryptionUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class EncryptionUtilTest {

    private SecretKey secretKey;

    @BeforeEach
    public void setUp() throws Exception {
        // Generate a mock key for testing purposes
        secretKey = EncryptionUtil.generateKey();
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String originalData = "Hello, World!";

        String encryptedData = EncryptionUtil.encrypt(originalData, secretKey);
        String decryptedData = EncryptionUtil.decrypt(encryptedData, secretKey);

        assertEquals(originalData, decryptedData);
    }

    @Test
    public void testEncryptAndDecryptEmptyString() throws Exception {
        String originalData = "";

        String encryptedData = EncryptionUtil.encrypt(originalData, secretKey);
        String decryptedData = EncryptionUtil.decrypt(encryptedData, secretKey);

        assertEquals(originalData, decryptedData);
    }

    @Test
    public void testEncryptAndDecryptLongString() throws Exception {
        String originalData = "This is a very long string to test encryption and decryption with AES.";

        String encryptedData = EncryptionUtil.encrypt(originalData, secretKey);
        String decryptedData = EncryptionUtil.decrypt(encryptedData, secretKey);

        assertEquals(originalData, decryptedData);
    }
}
