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

    private static final String PRIVATE_KEY_FILE = "src/main/java/com/HotelApp/util/encryptionUtil/keys/private_key.pem";
    private static final String PUBLIC_KEY_FILE = "src/main/java/com/HotelApp/util/encryptionUtil/keys/public_key.pem";
    private static final Logger log = LoggerFactory.getLogger(KeyService.class);

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            Path pathToKeys = Paths.get(PRIVATE_KEY_FILE);
            // Check if keys folder and files exist, if not, generate them
            if  (!Files.isDirectory(pathToKeys.getParent())) {
                Files.createDirectory(pathToKeys.getParent());
            }
            if (!Files.exists(pathToKeys)) {
                KeyGeneratorUtil.generateKeyPair();
                log.info(KEYS_ARE_GENERATED);
            }
            // Load the keys
            this.privateKey = loadPrivateKey();
            this.publicKey = loadPublicKey();
        } catch (Exception e) {
            log.error(ERROR_INITIALIZING_KEYS, e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            // Check if key files exist, if existed, destroy them
            Files.deleteIfExists(Path.of(PRIVATE_KEY_FILE));
            Files.deleteIfExists(Path.of(PUBLIC_KEY_FILE));
            log.info(KEYS_ARE_DESTROYED);
        } catch (Exception e) {
            log.error(ERROR_DESTROYING_KEYS, e.getMessage());
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
