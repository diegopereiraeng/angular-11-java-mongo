package com.bezkoder.spring.data.mongodb.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import com.bezkoder.spring.data.mongodb.dto.LocalUser;
import com.bezkoder.spring.data.mongodb.dto.SignUpRequest;
import com.bezkoder.spring.data.mongodb.exception.UserAlreadyExistAuthenticationException;
import com.bezkoder.spring.data.mongodb.model.User;

/**
 * @author Diego Pereira
 */
public interface UserService {

    public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
