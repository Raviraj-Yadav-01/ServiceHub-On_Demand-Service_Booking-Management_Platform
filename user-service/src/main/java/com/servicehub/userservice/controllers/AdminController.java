package com.servicehub.userservice.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminData(@RequestHeader("X-Auth-User-Role") String role) {

        // Check karein ki role ADMIN hai ya nahi
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Aapke paas Admin rights nahi hain!");
        }

        return ResponseEntity.ok("Welcome Admin! Ye raha aapka secret dashboard data.");
    }
}
