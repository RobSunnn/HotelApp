package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UsersRestController {

    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public List<UserEntity> allUsers() {
        return userService.findAllUsers();
    }


    @GetMapping("/{userEmail}")
    public UserEntity userByEmail(@PathVariable String userEmail) {

        return userService.findUserByEmail(userEmail);
    }


    @PostMapping("/makeUserAdmin/{email}")
    public String makeUserAdmin(@PathVariable String email) {

        userService.makeUserAdmin(email);

        return "redirect:/admin";
    }


    @PostMapping("/makeUserModerator/{email}")
    public String makeUserModerator(@PathVariable String email) {

        userService.makeUserModerator(email);

        return "redirect:/admin";
    }


    @PostMapping("/takeRights/{email}")
    public String takeRightsOfUser(@PathVariable String email) {

        userService.takeRights(email);

        return "redirect:/admin";
    }


}
