package com.HotelApp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.HotelApp.util.KeyManager.isKeyExpired;


public class KeyStorageUtil {

    private static final String KEY_FILE = "secret.key";

    public static void storeKey(String keyString) throws IOException {
        Files.write(Paths.get(KEY_FILE), keyString.getBytes());
        System.out.println("KEY IS STORED IN FROM THE STORAGE UTIL");
    }

    public static String retrieveKey() throws IOException {
        if (isKeyExpired()) {
            throw new RuntimeException("Key has expired or not set.");
        }
        System.out.println("KEY IS RETRIEVED FROM THE STORAGE UTIL");

        return new String(Files.readAllBytes(Paths.get(KEY_FILE)));
    }

}
