package com.ust.securerestapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ApiController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }

    @PostMapping("/secure")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String secure() {
        return "This is a secure API";
    }

}
