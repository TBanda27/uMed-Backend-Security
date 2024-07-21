package com.email_verification.adonis.service;

import com.email_verification.adonis.entity.User;
import com.email_verification.adonis.entity.UserResetPasswordToken;
import com.email_verification.adonis.enums.Role;
import com.email_verification.adonis.exception.BusinessValidationException;
import com.email_verification.adonis.mappers.UserToUserResultDto;
import com.email_verification.adonis.registration.UserRegistrationRequest;
import com.email_verification.adonis.registration.token.VerificationToken;
import com.email_verification.adonis.registration.token.VerificationTokenRepository;
import com.email_verification.adonis.repository.UserRepository;
import com.email_verification.adonis.repository.UserResetPasswordTokenRepository;
import com.email_verification.adonis.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserResetPasswordService {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserResetPasswordTokenRepository userResetPasswordTokenRepository;

    public String userResetPassword(String token, PasswordResetRequest passwordResetRequest) {
        User user = userService.getUser(passwordResetRequest.getEmail());
        if(!user.isEnabled())
            throw new BusinessValidationException(passwordResetRequest.getEmail() + " not yet enabled so cannot reset password");

       UserResetPasswordToken resetPasswordToken = getToken(token);
       if(user != resetPasswordToken.getUser())
           throw new BusinessValidationException("Token mismatch: Token is for a different user.");

       if(!userService.checkPasswordValidity(passwordResetRequest.getPassword()))
           throw new BusinessValidationException("Password should be valid. Follow the given password standard.");
       user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
       userRepository.save(user);

        return "Password for user with email: "+ passwordResetRequest.getEmail() + " has successfully been changed.";
    }

    public String userForgotPassword(String email) {
        User user = userService.getUser(email);

        if(!user.isEnabled())
            throw new BusinessValidationException("User with email: "+ email + " not yet enabled.");

        String token = UUID.randomUUID().toString();
        UserResetPasswordToken resetPasswordToken = new UserResetPasswordToken(user, token);
        userResetPasswordTokenRepository.save(resetPasswordToken);

//        Send email to reset password

        return "Email to reset password has been sent to : "+ user.getEmail();
    }

    UserResetPasswordToken getToken(String token){
        UserResetPasswordToken token1 = userResetPasswordTokenRepository.findByToken(token);

        log.info("Token : {}", token1);
        if(token1 == null)
            throw new BusinessValidationException("User Reset Password Token could not be retrieved properly");

        return token1;
    }

}
