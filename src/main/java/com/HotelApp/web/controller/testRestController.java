//package com.HotelApp.web.ControllerTest;
//
//import com.HotelApp.service.impl.TestService;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.UUID;
//
//
//@RestController
//public class testRestController {
//    private final TestService testService;
//
//    public testRestController(TestService testService) {
//        this.testService = testService;
//    }
//
//
//    //    @GetMapping("/js/info/{token}")
//    //@PathVariable("token") String token
//
//    @GetMapping("/js/info")
//    public ResponseEntity<List<String>> mailParams() {
//
////        if (token.equals("")) {
////           throw new ForbiddenUserException("not allowed");
////        }
//
//        List<String> test = testService.takeParams();
//
//        return ResponseEntity.ok(test);
//    }
//
//    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> token() {
//        String token = UUID.randomUUID().toString();
//        return ResponseEntity.ok("token:" + token);
//    }
//
//}
