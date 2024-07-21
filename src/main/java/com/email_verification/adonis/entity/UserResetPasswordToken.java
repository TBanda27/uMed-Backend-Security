package com.email_verification.adonis.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserResetPasswordToken {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date tokenExpirationTime;

    private static final int EXPIRATION_TIME = 86400;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserResetPasswordToken(String token) {
        this.token = token;
        this.tokenExpirationTime = calculateTokenExpirationTime();
    }

    public UserResetPasswordToken(User user, String token) {
        this.token = token;
        this.user = user;
        this.tokenExpirationTime = calculateTokenExpirationTime();
    }

    public Date calculateTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }

}
