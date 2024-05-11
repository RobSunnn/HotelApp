package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersRestController {

    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/allUsers")
    public List<UserEntity> allUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/allUsers/{userEmail}")
    public UserEntity userByEmail(@PathVariable String userEmail) {

        return userService.findUserByEmail(userEmail);
    }


}
