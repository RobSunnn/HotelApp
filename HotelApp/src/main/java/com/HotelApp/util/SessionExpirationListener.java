package com.HotelApp.util;


import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;


public class SessionExpirationListener implements HttpSessionListener {

    private static final String KEY_FILE = "secret.key";
    private static final Logger log = LoggerFactory.getLogger(SessionExpirationListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("Session created");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("Session destroyed");
        try {
            Files.deleteIfExists(Paths.get(KEY_FILE));
            log.info("file deleted Session Event Listener");
        } catch (Exception ignored) {

        }
    }
}
