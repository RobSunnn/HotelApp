package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.service.UserService;
import com.HotelApp.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UsersRestController {

    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserEntity>> allUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }


    @GetMapping("/{userEmail}")
    public ResponseEntity<UserEntity> userByEmail(@PathVariable String userEmail) {
//todo: make userView
        UserEntity user = userService.findUserByEmail(userEmail);

        return ResponseEntity.ok(user);

    }

    @PostMapping("/makeUserAdmin/{email}")
    public ResponseEntity<String> makeUserAdmin(@PathVariable String email) {
        userService.makeUserAdmin(email);

        return ResponseEntity.ok("redirect:/admin");
    }

    @PostMapping("/makeUserModerator/{email}")
    public ResponseEntity<String> makeUserModerator(@PathVariable String email) {
        userService.makeUserModerator(email);

        return ResponseEntity.ok("redirect:/admin");
    }

    @PostMapping("/takeRights/{email}")
    public ResponseEntity<String> takeRightsOfUser(@PathVariable String email) {
        userService.takeRights(email);

        return ResponseEntity.ok("redirect:/admin");
    }

}
