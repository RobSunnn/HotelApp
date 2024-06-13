package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.service.UserRoleChangeRequest;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UsersRestController {

    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/allUsers")
//    public ResponseEntity<List<UserView>> allUsers() {
//        List<UserView> allUsers = userService.findAllUsers();
//        return ResponseEntity.ok(allUsers);
//    }


    @GetMapping("/info")
    public ResponseEntity<UserView> userByEmail(@RequestParam("encrypted") String encryptedEmail) {
        // Decrypt email if needed (assuming your logic for decryption)
        UserView user = userService.findUserByEmail(encryptedEmail);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/changeUserRoles")
    public ResponseEntity<String> makeUserAdmin(@RequestBody UserRoleChangeRequest request) {
        userService.changeUserRole(request.getEncrypted(), request.getCommand());

        return ResponseEntity.ok("redirect:/admin");
    }


}
