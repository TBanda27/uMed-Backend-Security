package com.email_verification.adonis.controller;


import com.email_verification.adonis.registration.token.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
@AllArgsConstructor
@Slf4j
public class UserVerifyEmailController {

    private final VerificationTokenService verificationTokenService;

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){

        log.info("Request to verify email with token: {}", token);

        if(verificationTokenService.verifyEmail(token))
            return ResponseEntity.ok("Email successfully verified,  you can now log into your account.");
        else
            return ResponseEntity.ok("Email could not be verified.");
    }
}
