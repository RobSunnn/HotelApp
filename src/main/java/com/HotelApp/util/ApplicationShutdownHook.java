package com.HotelApp.util;


import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void deleteFileOnStartup() {
        deleteFileIfExists();
    }

    @PreDestroy
    public void onShutdown() {
        deleteFileIfExists();
    }

    private void deleteFileIfExists() {
        try {
            Files.deleteIfExists(Paths.get(ApplicationShutdownHook.KEY_FILE));
            log.info("File '{}' deleted successfully during startup.", ApplicationShutdownHook.KEY_FILE);
        } catch (Exception e) {
            log.error("Error deleting file '{}': {}", KEY_FILE, e.getMessage());
        }
    }
}
