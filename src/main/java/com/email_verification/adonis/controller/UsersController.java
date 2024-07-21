package com.email_verification.adonis.controller;

import com.email_verification.adonis.service.UserService;
import com.email_verification.adonis.user.UserResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class UsersController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResultDto>> getUsers(){
        log.info("Request to get all users: ");
        return ResponseEntity.ok(userService.findAllUsers());
    }



    @GetMapping("/home")
    public ResponseEntity<String> home(){
        log.info("Request to visit the home page: ");
        return ResponseEntity.ok("This is the homepage of uMed Backend Security System");
    }
}
