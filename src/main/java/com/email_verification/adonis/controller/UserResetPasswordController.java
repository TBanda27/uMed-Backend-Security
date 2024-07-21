package com.email_verification.adonis.controller;

import com.email_verification.adonis.service.UserResetPasswordService;
import com.email_verification.adonis.user.PasswordResetRequest;
import com.email_verification.adonis.user.UserForgotPassword;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserResetPasswordController {
    private final UserResetPasswordService userResetPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody UserForgotPassword userForgotPassword)
    {
        log.info("User with email: {} forgot password", userForgotPassword.getEmail());
        return ResponseEntity.ok(userResetPasswordService.userForgotPassword(userForgotPassword.getEmail()));
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable("token") String token,
                                                @RequestBody PasswordResetRequest passwordResetRequest){
        log.info("Request to reset password for email: {}", passwordResetRequest.getEmail());
        return ResponseEntity.ok(userResetPasswordService.userResetPassword(token, passwordResetRequest));
    }
}
