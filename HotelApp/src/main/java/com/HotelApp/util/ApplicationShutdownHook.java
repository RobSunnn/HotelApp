//package com.HotelApp.util;
//
//
//import jakarta.annotation.PreDestroy;
//import org.springframework.stereotype.Component;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Component
//public class ApplicationShutdownHook {
//
//    private static final String KEY_FILE = "secret.key";
//
//    @PreDestroy
//    public void onShutdown() {
//        try {
//            Files.deleteIfExists(Paths.get(KEY_FILE));
//            System.out.println("file deleted hooOK");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
