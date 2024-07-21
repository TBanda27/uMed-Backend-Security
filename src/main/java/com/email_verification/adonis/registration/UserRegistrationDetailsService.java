package com.email_verification.adonis.registration;

import com.email_verification.adonis.repository.UserRepository;
import com.email_verification.adonis.repository.UserResetPasswordTokenRepository;
import com.email_verification.adonis.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegistrationDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User with email: "+ email + " not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .failureUrl("/login?error=true")
//                        .permitAll())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/perform-logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .logoutSuccessHandler(new LogoutSuccessHandler() {
                            @Override
                            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                                request.getSession().invalidate();
                            }
                        })
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/registration/**",
                                "/api/v1/login/**",
                                "/api/v1/forgot-password/**",
                                "/api/v1/verify-email",
                                "/api/v1/reset-password/**",
                                "/api/v1/registration",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-resources/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }
}
