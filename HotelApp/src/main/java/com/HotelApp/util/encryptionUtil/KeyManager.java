package com.HotelApp.util.encryptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class KeyManager {

    private static final String SECRET_KEY_FILE = "secret.key";
    private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(10);
    private static final Logger log = LoggerFactory.getLogger(KeyManager.class);

    private static SecretKey secretKey;
    private static Instant keyUpdateTime;

    static {
        try {
            if (!Files.exists(Paths.get(SECRET_KEY_FILE))) {
                // Generate and store the key if it doesn't exist
                regenerateKey();
                log.info("KEY IS REGENERATED FROM SAFE FILE");
            } else {
                // Retrieve the stored key
                String keyString = KeyStorageUtil.retrieveKey();
                secretKey = EncryptionUtil.stringToKey(keyString);
                keyUpdateTime = Instant.now(); // Assume the key is freshly loaded
                log.info("KEY IS SAVED IN A SAFE FILE");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize encryption key", e);
        }
    }

    public static SecretKey getSecretKey() {
        // Check if key has expired, regenerate if needed
        if (isKeyExpired()) {
            try {
                regenerateKey();
            } catch (Exception e) {
                throw new RuntimeException("Failed to regenerate encryption key", e);
            }
        }
        log.info("SPRING TAKES KEY");

        return secretKey;
    }

    protected static boolean isKeyExpired() {
        // If keyUpdateTime is null, it means the key has never been set or expired
        if (keyUpdateTime == null) {
            log.info("KEY IS EXPIRED");
            return true;
        }
        // Calculate the expiration time by adding the expiration duration to the last update time
        Instant expirationTime = keyUpdateTime.plus(EXPIRATION_DURATION);
        // Check if the current time is after the calculated expiration time
        return Instant.now().isAfter(expirationTime);
    }

    static void regenerateKey() throws Exception {
        // Generate a new key
        secretKey = EncryptionUtil.generateKey();
        log.info("KEY IS GENERATED");
        // Store the new key string
        String keyString = EncryptionUtil.keyToString(secretKey);
        KeyStorageUtil.storeKey(keyString);
        // Update the key update time to now
        keyUpdateTime = Instant.now();
    }
}
