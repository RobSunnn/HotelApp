package com.HotelApp.util.encryptionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class KeyGeneratorUtil {

    private static final String PRIVATE_KEY_PATH = "com/HotelApp/util/encryptionUtil/";
    private static final String PUBLIC_KEY_PATH = "com/HotelApp/util/encryptionUtil/";

    public static void generateKeyPair() throws NoSuchAlgorithmException, IOException {
        // Ensure directories exist
        new File(PRIVATE_KEY_PATH).getParentFile().mkdirs();
        new File(PUBLIC_KEY_PATH).getParentFile().mkdirs();

        // Generate an RSA key pair
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048); // Use 2048-bit key for better security
        KeyPair pair = keyPairGen.generateKeyPair();

        // Get the private and public keys
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String privateKeyFile = PRIVATE_KEY_PATH + "private_key.pem";
        String publicKeyFile = PUBLIC_KEY_PATH + "public_key.pem";

        // Write the private key to a file
        try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
            fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            fos.write(Base64.getMimeEncoder(64, "\n".getBytes()).encode(privateKey.getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----".getBytes());
        }

        // Write the public key to a file
        try (FileOutputStream fos = new FileOutputStream(publicKeyFile)) {
            fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            fos.write(Base64.getMimeEncoder(64, "\n".getBytes()).encode(publicKey.getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----".getBytes());
        }
    }
}
