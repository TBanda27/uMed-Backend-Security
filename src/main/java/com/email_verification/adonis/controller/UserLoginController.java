package com.email_verification.adonis.controller;


import com.email_verification.adonis.service.UserResetPasswordService;
import com.email_verification.adonis.service.UserService;
import com.email_verification.adonis.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@AllArgsConstructor
@Slf4j
public class UserLoginController {

    private final UserService userService;

    @PostMapping()
    ResponseEntity<String> login(@RequestBody UserLoginDetails loginRequest){

        log.info("Request to log in user with username: {}", loginRequest.getEmail());
        return ResponseEntity.ok(userService.login(loginRequest));
    }


}
