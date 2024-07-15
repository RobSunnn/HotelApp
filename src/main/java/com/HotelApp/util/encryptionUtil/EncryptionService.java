package com.HotelApp.util.encryptionUtil;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class EncryptionService {
    private final KeyService keyService;

    public EncryptionService(KeyService keyService) {
        this.keyService = keyService;
    }

    public String decrypt(String encryptedData) {
        try {
            PrivateKey privateKey = keyService.getPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception ignored) {
            return "";
        }
    }

    //    public String decrypt(String encryptedData) throws Exception {
//        PrivateKey privateKey = keyService.getPrivateKey();
//        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
//        cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
//        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
//        byte[] decryptedData = cipher.doFinal(decodedData);
//        return new String(decryptedData, StandardCharsets.UTF_8);
//    }
    public String encrypt(String data) throws Exception {
        PublicKey publicKey = keyService.getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(encryptedData);
    }
}