package com.email_verification.adonis.controller;


import com.email_verification.adonis.entity.User;
import com.email_verification.adonis.event.RegistrationCompleteEvent;
import com.email_verification.adonis.mappers.UserResultDtoToUser;
import com.email_verification.adonis.registration.UserRegistrationRequest;
import com.email_verification.adonis.registration.token.VerificationTokenService;
import com.email_verification.adonis.service.UserResetPasswordService;
import com.email_verification.adonis.service.UserService;
import com.email_verification.adonis.user.UserResultDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/registration")
@AllArgsConstructor
@Slf4j
public class UserRegistrationController {
    public final UserService userService;

    public final UserResultDtoToUser toUser;

    public final VerificationTokenService tokenService;

    public final ApplicationEventPublisher publisher;

    @PostMapping()
    ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest,
                                        final HttpServletRequest request){

        log.info("Request to register new user with : "+ userRegistrationRequest.toString());
        User user = userService.registerUser(userRegistrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
         return ResponseEntity.ok("Please check your email to verify your email and complete registration");
    }


    public String applicationUrl(HttpServletRequest request) {

        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
