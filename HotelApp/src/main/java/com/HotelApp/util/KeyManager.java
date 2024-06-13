package com.HotelApp.util;


import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class KeyManager {

    private static final String SECRET_KEY_FILE = "secret.key";
    private static final Duration EXPIRATION_DURATION = Duration.ofSeconds(10);

    private static SecretKey secretKey;
    private static Instant keyUpdateTime;

    static {
        try {
            if (!Files.exists(Paths.get(SECRET_KEY_FILE))) {
                // Generate and store the key if it doesn't exist
                regenerateKey();
                System.out.println("KEY IS REGENERATED FROM SAFE FILE");
            } else {
                // Retrieve the stored key
                String keyString = KeyStorageUtil.retrieveKey();
                secretKey = EncryptionUtil.stringToKey(keyString);
                keyUpdateTime = Instant.now(); // Assume the key is freshly loaded
                System.out.println("KEY IS SAVED IN A SAFE FILE");

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
        System.out.println("SPRING TAKES KEY");
        return secretKey;
    }

    protected static boolean isKeyExpired() {
        // If keyUpdateTime is null, it means the key has never been set or expired
        if (keyUpdateTime == null) {
            System.out.println("KEY IS EXPIRED");
            return true;
        }

        // Calculate the expiration time by adding the expiration duration to the last update time
        Instant expirationTime = keyUpdateTime.plus(EXPIRATION_DURATION);

        // Check if the current time is after the calculated expiration time
        return Instant.now().isAfter(expirationTime);
    }

    private static void regenerateKey() throws Exception {
        // Generate a new key
        secretKey = EncryptionUtil.generateKey();
        System.out.println("KEY IS GENERATED");
        // Store the new key string
        String keyString = EncryptionUtil.keyToString(secretKey);
        KeyStorageUtil.storeKey(keyString);

        // Update the key update time to now
        keyUpdateTime = Instant.now();
    }
}
