package com.example.locker14;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j // adds to class parameter 'log'
@RestController
public class LockerController {

    // будет открыт без аутентификации
    @GetMapping("/open")
    public String open() {
        return "open";
    }

    // потребует аутентификации
    @GetMapping("/secure")
    public String secure(
            Principal principal // - this is authorised user
    ) {
        // Получить некий объект "пользователь"
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Проверяем, является ли Principal экземпляром UserData
        UserData userData = null;
        if (user instanceof UserData) {
            log.info("xxxx " + principal.getName()); // залогировать имя пользователя
            // Если да - привести его объект к UserData
            userData = (UserData) user;
//            log.info("xxxx " + userData.getUsername()); // это заменяется principal.getName()
            log.info("xxxx " + userData.getAuthorities()); // залогировать роль пользователя
        }
        return "secure";
    }

    // только для роли ADMIN
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
