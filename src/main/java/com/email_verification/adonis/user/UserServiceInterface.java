package com.email_verification.adonis.user;


import com.email_verification.adonis.entity.User;
import com.email_verification.adonis.registration.UserRegistrationRequest;

import java.util.List;

public interface UserServiceInterface {

    List<UserResultDto> findAllUsers();

    User registerUser(UserRegistrationRequest userRegistrationRequest);

    UserResultDto getUserByEmail(String email);

    void saveUserVerificationToken(User user, String verificationToken);

    String login(UserLoginDetails userLoginDetails);

    User findByEmailAndPassword(String email, String password);
}
