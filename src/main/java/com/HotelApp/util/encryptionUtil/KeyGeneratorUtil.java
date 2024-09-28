package com.HotelApp.util.encryptionUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static void generateKeyPair(Path privateKeyPath, Path publicKeyPath) throws NoSuchAlgorithmException, IOException {
        // Generate an RSA key pair
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair pair = keyPairGen.generateKeyPair();

        // Get the private and public keys
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // Write the private key to a file
        try (FileOutputStream fos = new FileOutputStream(privateKeyPath.toFile())) {
            fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(privateKey.getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----".getBytes());
        }

        // Write the public key to a file
        try (FileOutputStream fos = new FileOutputStream(publicKeyPath.toFile())) {
            fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(publicKey.getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----".getBytes());
        }
    }
}
