package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UsersRestController {

private final HotelService hotelService;

    public UsersRestController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserView>> allUsers() {
        List<UserView> allUsers = hotelService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }


    @GetMapping("/{userEmail}")
    public ResponseEntity<UserView> userByEmail(@PathVariable String userEmail) {

        UserView user = hotelService.findUserByEmail(userEmail);

        return ResponseEntity.ok(user);

    }

    @PostMapping("/makeUserAdmin/{email}")
    public ResponseEntity<String> makeUserAdmin(@PathVariable String email) {
        hotelService.makeUserAdmin(email);

        return ResponseEntity.ok("redirect:/admin");
    }

    @PostMapping("/makeUserModerator/{email}")
    public ResponseEntity<String> makeUserModerator(@PathVariable String email) {
        hotelService.makeUserModerator(email);

        return ResponseEntity.ok("redirect:/admin");
    }

    @PostMapping("/takeRights/{email}")
    public ResponseEntity<String> takeRightsOfUser(@PathVariable String email) {
        hotelService.takeRightsOfUser(email);

        return ResponseEntity.ok("redirect:/admin");
    }

}
