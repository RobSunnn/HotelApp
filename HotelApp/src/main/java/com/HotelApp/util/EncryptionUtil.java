package com.HotelApp.util;

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

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // for AES-256
        System.out.println("encrUtil generates key");
        return keyGen.generateKey();
    }

    // Encrypt a string

    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, KeyManager.getSecretKey());
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        System.out.println("encrUtil enctrypt info");

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, KeyManager.getSecretKey());
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        System.out.println("encrUtil decrypt info");

        return new String(decryptedBytes);
    }

    public static String decrypt(String encryptedData, String Base64Iv, String key) throws Exception {
        byte[] iv = Base64.getDecoder().decode(Base64Iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] sharedSecretKey = Base64.getDecoder().decode(key);
        SecretKeySpec keySpec = new SecretKeySpec(sharedSecretKey, ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM + "/CBC/PKCS5Padding"); // Specify mode and padding
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String keyToString(SecretKey secretKey) {
        System.out.println("keytoString from encrUtil");
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey stringToKey(String keyString) {
        System.out.println("stringToKey from encrUtil");

        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}