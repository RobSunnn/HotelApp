package com.HotelApp.util.encryptionUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class KeyGeneratorUtil {

    private static final String KEYS_DIR = "target/classes/com/HotelApp/util/encryptionUtil/keys";
    private static final String PRIVATE_KEY_FILE = KEYS_DIR + "/private_key.pem";
    private static final String PUBLIC_KEY_FILE = KEYS_DIR + "/public_key.pem";

    public static void generateKeyPair() throws NoSuchAlgorithmException, IOException {
        // Generate an RSA key pair
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair pair = keyPairGen.generateKeyPair();

        // Get the private and public keys
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // Create keys directory if it doesn't exist
        Path keysDirectory = Paths.get(KEYS_DIR);
        if (!Files.isDirectory(keysDirectory)) {
            Files.createDirectories(keysDirectory);
        }

        // Write the private key to a file
        try (FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_FILE)) {
            fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(privateKey.getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----".getBytes());
        }

        // Write the public key to a file
        try (FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILE)) {
            fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(publicKey.getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----".getBytes());
        }
    }
}
