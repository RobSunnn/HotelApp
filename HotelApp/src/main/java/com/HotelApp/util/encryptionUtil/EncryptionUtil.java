package com.HotelApp.util.encryptionUtil;

import com.HotelApp.util.SessionExpirationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final Logger log = LoggerFactory.getLogger(EncryptionUtil.class);


    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // for AES-256
        log.info("Encryption Util generates key");
        return keyGen.generateKey();
    }

    // Encrypt a string

    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, KeyManager.getSecretKey());
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        log.info("Encryption Util encrypts info");

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, KeyManager.getSecretKey());
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        log.info("Encryption Util decrypts info");


        return new String(decryptedBytes);
    }

    public static String decrypt(String encryptedData, String Base64Iv, String key) throws Exception {
        byte[] iv = Base64.getDecoder().decode(Base64Iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] sharedSecretKey = Base64.getDecoder().decode(key);
        SecretKeySpec keySpec = new SecretKeySpec(sharedSecretKey, ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM + "/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        log.info("Encryption Util decrypts info from front end");
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String keyToString(SecretKey secretKey) {
        log.info("Key to String from Encryption Util");
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey stringToKey(String keyString) {
        log.info("String To Key from Encryption Util");

        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}