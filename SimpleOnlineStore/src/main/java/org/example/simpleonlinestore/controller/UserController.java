package org.example.simpleonlinestore.controller;

import jakarta.validation.Valid;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/addresses")
    public ResponseEntity<User> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,@Valid @RequestBody AddressDTO addressDTO){
        User updatedUser=userService.addAddress(userDetails.getUsername(),addressDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
