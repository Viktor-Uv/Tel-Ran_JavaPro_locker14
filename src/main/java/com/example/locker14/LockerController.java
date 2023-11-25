package com.example.locker14;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LockerController {

    // будет открыт без аутентификации
    @GetMapping("/open")
    public String open() {
        return "open";
    }

    // потребует аутентификации
    @GetMapping("/secure")
    public String secure() {
        return "secure";
    }

    // только для роли ADMIN
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
