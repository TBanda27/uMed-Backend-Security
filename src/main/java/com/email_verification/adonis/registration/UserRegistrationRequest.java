package com.email_verification.adonis.registration;

import com.email_verification.adonis.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}
