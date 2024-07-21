package com.email_verification.adonis.service;

import com.email_verification.adonis.entity.CustomOAuth2User;
import com.email_verification.adonis.entity.User;
import com.email_verification.adonis.exception.BusinessValidationException;
import com.email_verification.adonis.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        System.out.println("OAuth User loading: "+ userRequest.toString());
        log.info("OAuth user loading {}", userRequest);
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || email.isEmpty()) {
            throw new BusinessValidationException("OAuth user email attribute cannot be empty");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            log.info("User already exists: " + user.getEmail());
        } else {
            user = new User();
            user.setEmail(email);
            user.setFirstName(name != null ? name.split(" ")[0] : "Unknown");
            user.setLastName(name != null && name.split(" ").length > 1 ? name.split(" ")[1] : "");
            user.setProvider(registrationId);
            Object providerIdObj = oAuth2User.getAttribute(userNameAttributeName);
            if (providerIdObj instanceof String) {
                user.setProviderId((String) providerIdObj);
            } else if (providerIdObj != null) {
                user.setProviderId(providerIdObj.toString());
            } else {
                log.info("Provider ID is null for user: " + email);
            }
            user.setEnabled(true); // Assuming OAuth users are enabled by default
            userRepository.save(user);
            log.info("New OAuth user created: " + user.getEmail());
        }

        return new CustomOAuth2User(Collections.singletonList(new SimpleGrantedAuthority("USER")), oAuth2User.getAttributes(), userNameAttributeName, user);
    }

}

