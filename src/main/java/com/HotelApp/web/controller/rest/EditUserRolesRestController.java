package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.service.UserRoleChangeRequest;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
public class EditUserRolesRestController {

    private final UserService userService;

    public EditUserRolesRestController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info")
    public ResponseEntity<?> userByEmail(@RequestParam("encrypted") String encryptedEmail) {
        UserView user = userService.findUserByEmail(encryptedEmail);
        return ResponseEntity.ok(user.getRoleNames());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/secret")
    public ResponseEntity<?> changeUserRole(@RequestBody UserRoleChangeRequest request) {
        userService.changeUserRole(request.getEncrypted(), request.getCommand());
        return ResponseEntity.ok(new ModelAndView("redirect:/admin"));
    }

}
