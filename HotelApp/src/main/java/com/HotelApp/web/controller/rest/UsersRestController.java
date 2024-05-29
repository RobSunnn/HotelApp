package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserView>> allUsers() {
        List<UserView> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }


    @GetMapping("/{userEmail}")
    public ResponseEntity<UserView> userByEmail(@PathVariable String userEmail) {

        UserView user = userService.findUserByEmail(userEmail);

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
