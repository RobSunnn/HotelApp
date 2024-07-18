package com.HotelApp.util.encryptionUtil;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class KeyService {

    private static final String PRIVATE_KEY_FILE = "com/HotelApp/util/encryptionUtil/keys/private_key.pem";
    private static final String PUBLIC_KEY_FILE = "com/HotelApp/util/encryptionUtil/keys/public_key.pem";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            // Check if key files exist, if not, generate them
            if (!Files.exists(Paths.get(PRIVATE_KEY_FILE)) || !Files.exists(Paths.get(PUBLIC_KEY_FILE))) {
                KeyGeneratorUtil.generateKeyPair();
            }
            // Load the keys
            this.privateKey = loadPrivateKey();
            this.publicKey = loadPublicKey();


        } catch (Exception e) {
            System.err.println("Error initializing KeyService: " + e.getMessage());
        }
    }

    private PrivateKey loadPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(KeyService.PRIVATE_KEY_FILE));
        String privateKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    private PublicKey loadPublicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(KeyService.PUBLIC_KEY_FILE));
        String publicKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
