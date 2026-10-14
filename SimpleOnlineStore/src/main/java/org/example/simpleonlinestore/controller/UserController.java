package org.example.simpleonlinestore.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.service.impl.auth.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService){
        this.userService=userService;
    }
    @PostMapping("/add-address")
    public ResponseEntity<User> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,@Valid @RequestBody AddressDTO addressDTO){
        User updatedUser=userService.addAddress(userDetails.getUsername(),addressDTO);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAddresses(){
        List<Address> addressList=userService.getAllAddresses();
        return ResponseEntity.ok(addressList);
    }
    @DeleteMapping("/delete-address/{addressId}")
    public ResponseEntity<User> removeAddress(@PathVariable Long addressId){
        User updatedUser=userService.removeAddress(addressId);
        return ResponseEntity.ok(updatedUser);
    }
}