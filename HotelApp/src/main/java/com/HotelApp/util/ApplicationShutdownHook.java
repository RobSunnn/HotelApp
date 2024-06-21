package com.HotelApp.util;


import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ApplicationShutdownHook {

    private static final String KEY_FILE = "secret.key";
    private static final Logger log = LoggerFactory.getLogger(ApplicationShutdownHook.class);

    @PreDestroy
    public void onShutdown() {
        try {
            Files.deleteIfExists(Paths.get(KEY_FILE));
            log.info("file deleted hooOK");
        } catch (Exception ignored) {
        }
    }
}
