package com.HotelApp.util.encryptionUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class KeyGeneratorUtil {

    private static final String PRIVATE_KEY_PATH = "com/HotelApp/util/encryptionUtil/";
    private static final String PUBLIC_KEY_PATH = "com/HotelApp/util/encryptionUtil/";

    public static void generateKeyPair() throws NoSuchAlgorithmException, IOException {
        // Generate an RSA key pair
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair pair = keyPairGen.generateKeyPair();

        // Get the private and public keys
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // Write the private key to a file
        try (FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_PATH)) {
            fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(privateKey.getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----".getBytes());
        }

        // Write the public key to a file
        try (FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_PATH)) {
            fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(publicKey.getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----".getBytes());
        }
    }
}
