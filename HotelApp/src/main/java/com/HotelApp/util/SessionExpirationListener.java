package com.HotelApp.util;


import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionExpirationListener implements HttpSessionListener {

    private static final String KEY_FILE = "secret.key";
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session created");
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session destroyed");

        try {
            Files.deleteIfExists(Paths.get(KEY_FILE));
            System.out.println("file deleted SEL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
