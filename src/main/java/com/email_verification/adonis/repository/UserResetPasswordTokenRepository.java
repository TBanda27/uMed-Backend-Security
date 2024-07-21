package com.email_verification.adonis.repository;

import com.email_verification.adonis.entity.User;
import com.email_verification.adonis.entity.UserResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserResetPasswordTokenRepository extends JpaRepository<UserResetPasswordToken, Long> {

    UserResetPasswordToken findByToken(String token);

}
