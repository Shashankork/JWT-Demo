package com.example.jw.controller;

import com.example.jw.annotation.Authorized;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Authorized(scope = "ADMIN")
    @GetMapping("/admin")
    public String adminEndpoint() {

        return "This is ADMIN-only endpoint!";
    }

    @Authorized(scope = "USER")
    @GetMapping("/user")
    public String userEndpoint() {

        return "This is USER-only endpoint!";
    }

    @Authorized(scope = "ADMIN")
    @PostMapping("/create")
    public String createData(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String description = payload.get("description");

        return "Data created successfully! Title: " + title + ", Description: " + description;
    }

}
