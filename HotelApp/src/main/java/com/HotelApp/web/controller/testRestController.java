package com.HotelApp.web.controller;

import com.HotelApp.service.exception.ForbiddenUserException;
import com.HotelApp.service.impl.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;



@RestController
public class testRestController {
    private final TestService testService;

    public testRestController(TestService testService) {
        this.testService = testService;
    }


//    @GetMapping("/js/info/{token}")
    //@PathVariable("token") String token
    @GetMapping("/js/info")
    public ResponseEntity<List<String>> mailParams() {

//        if (token.equals("")) {
//           throw new ForbiddenUserException("not allowed");
//        }

        List<String> test = testService.takeParams();

        return ResponseEntity.ok(test);
    }

    @GetMapping("/token")
    public String token() {
        String token = UUID.randomUUID().toString();
        return token;
    }

}
