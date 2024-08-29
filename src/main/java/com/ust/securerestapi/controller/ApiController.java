package com.ust.securerestapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ApiController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String hello() {
        return "Hello, World! This is a User.";
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String secure() {
        return "This is a secure API";
    }

}
