package com.HotelApp.util.encryptionUtil;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class KeyService {

    private static final String PRIVATE_KEY_FILE = "com/HotelApp/util/encryptionUtil/private_key.pem";
    private static final String PUBLIC_KEY_FILE = "com/HotelApp/util/encryptionUtil/public_key.pem";
    private static final Logger log = LoggerFactory.getLogger(KeyService.class);

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            // Log the paths for debugging
            log.info("Private key file path: {}", PRIVATE_KEY_FILE);
            log.info("Public key file path: {}", PUBLIC_KEY_FILE);

            Path privateKeyPath = Paths.get(PRIVATE_KEY_FILE);
            Path publicKeyPath = Paths.get(PUBLIC_KEY_FILE);

            // Ensure the directories exist
            Files.createDirectories(privateKeyPath.getParent());
            Files.createDirectories(publicKeyPath.getParent());

            // Check if key files exist, if not, generate them
            if (!Files.exists(privateKeyPath) || !Files.exists(publicKeyPath)) {
                KeyGeneratorUtil.generateKeyPair();
                log.info("Keys are generated.");
            }

            // Load the keys
            this.privateKey = loadPrivateKey();
            this.publicKey = loadPublicKey();
        } catch (Exception e) {
            log.info("Error initializing keys: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            // Check if key files exist, if existed, destroy them
                Files.deleteIfExists(Path.of(PRIVATE_KEY_FILE));
                Files.deleteIfExists(Path.of(PUBLIC_KEY_FILE));
                log.info("Keys are destroyed.");
        } catch (Exception e) {
            log.info("Error destroying keys: {}", e.getMessage());
        }
    }

    private PrivateKey loadPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
        String privateKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", ""); // Remove newlines and spaces
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    private PublicKey loadPublicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));
        String publicKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // Remove newlines and spaces
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
