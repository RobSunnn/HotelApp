package com.HotelApp.util.encryptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import static com.HotelApp.util.encryptionUtil.KeyManager.isKeyExpired;


public class KeyStorageUtil {

    private static final String KEY_FILE = "secret.key";
    private static final Logger log = LoggerFactory.getLogger(KeyStorageUtil.class);


    public static void storeKey(String keyString) throws IOException {
        Files.write(Paths.get(KEY_FILE), keyString.getBytes());
        log.info("KEY IS STORED IN FROM THE STORAGE UTIL");
    }

    public static String retrieveKey() throws IOException {
        if (isKeyExpired()) {
            throw new NoSuchFileException("Key has expired or not set.");
        }
        log.info("KEY IS RETRIEVED FROM THE STORAGE UTIL");
        return new String(Files.readAllBytes(Paths.get(KEY_FILE)));
    }

}
