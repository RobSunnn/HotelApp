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

import static com.HotelApp.common.constants.FailConstants.ERROR_DESTROYING_KEYS;
import static com.HotelApp.common.constants.FailConstants.ERROR_INITIALIZING_KEYS;
import static com.HotelApp.common.constants.SuccessConstants.KEYS_ARE_DESTROYED;
import static com.HotelApp.common.constants.SuccessConstants.KEYS_ARE_GENERATED;

@Service
public class KeyService {

    private static final Logger log = LoggerFactory.getLogger(KeyService.class);

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            // Define the paths for key files in a temporary directory
            Path privateKeyPath = Paths.get(System.getProperty("java.io.tmpdir"), "private_key.pem");
            Path publicKeyPath = Paths.get(System.getProperty("java.io.tmpdir"), "public_key.pem");

            // Generate keys if they do not exist
            if (!Files.exists(privateKeyPath)) {
                KeyGeneratorUtil.generateKeyPair(privateKeyPath, publicKeyPath);
                log.info(KEYS_ARE_GENERATED);
            } else {
                log.info("Keys already exist, loading them.");
            }

            // Load the keys
            this.privateKey = loadPrivateKey(privateKeyPath);
            this.publicKey = loadPublicKey(publicKeyPath);
        } catch (Exception e) {
            log.error(ERROR_INITIALIZING_KEYS, e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            // Define the paths for key files in the temporary directory
            Path privateKeyPath = Paths.get(System.getProperty("java.io.tmpdir"), "private_key.pem");
            Path publicKeyPath = Paths.get(System.getProperty("java.io.tmpdir"), "public_key.pem");

            // Check if key files exist, if they do, delete them
            Files.deleteIfExists(privateKeyPath);
            Files.deleteIfExists(publicKeyPath);
            log.info(KEYS_ARE_DESTROYED);
        } catch (Exception e) {
            log.error(ERROR_DESTROYING_KEYS, e.getMessage());
        }
    }

    private PrivateKey loadPrivateKey(Path privateKeyPath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(privateKeyPath);
        String privateKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    private PublicKey loadPublicKey(Path publicKeyPath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(publicKeyPath);
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
