package com.HotelApp.web.controller.rest;

import com.HotelApp.util.encryptionUtil.KeyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.Base64;

@RestController
public class PublicKeyRestController {
    private final KeyService keyService;

    public PublicKeyRestController(KeyService keyService) {
        this.keyService = keyService;
    }

    @GetMapping("/get-public-key")
    public String getPublicKey() {
        PublicKey publicKey = keyService.getPublicKey();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}
