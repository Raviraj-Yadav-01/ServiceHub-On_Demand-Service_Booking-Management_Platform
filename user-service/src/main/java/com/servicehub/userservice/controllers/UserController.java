package com.servicehub.userservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/profile")
    public String getUserProfile(@RequestHeader("loggedInUser") String username) {
        return "Secure profile accessed by: " + username;
    }
}