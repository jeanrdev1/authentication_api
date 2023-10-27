package com.develop.authentication_api.controller.unsafe;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TestController.PATH)
public class TestController {
    
    public static final String PATH = "unsafe/test";

    @GetMapping
    public String getAuthenticated() {
        return LocalDateTime.now().toString();
    }
}
