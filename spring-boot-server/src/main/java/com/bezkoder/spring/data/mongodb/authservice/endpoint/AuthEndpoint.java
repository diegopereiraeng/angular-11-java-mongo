package com.bezkoder.spring.data.mongodb.authservice.endpoint;


import com.bezkoder.spring.data.mongodb.authservice.exception.BadRequestException;
import com.bezkoder.spring.data.mongodb.authservice.exception.EmailAlreadyExistsException;
import com.bezkoder.spring.data.mongodb.authservice.exception.UsernameAlreadyExistsException;
import com.bezkoder.spring.data.mongodb.authservice.model.Profile;
import com.bezkoder.spring.data.mongodb.authservice.model.Role;
import com.bezkoder.spring.data.mongodb.authservice.model.User;
import com.bezkoder.spring.data.mongodb.authservice.payload.*;
import com.bezkoder.spring.data.mongodb.authservice.service.FacebookService;
import com.bezkoder.spring.data.mongodb.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
@CrossOrigin(origins = "http://angular.harness-demo.site")
@RestController
@Slf4j
public class AuthEndpoint {

    @Autowired private UserService userService;
    @Autowired private FacebookService facebookService;

    @GetMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> auth(Principal user) {
        log.info("auth user");

        return ResponseEntity
                .ok(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping("/facebook/signin")
    public  ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        log.info("facebook login {}", facebookLoginRequest);
        String token = facebookService.loginUser(facebookLoginRequest.getAccessToken());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest payload) {
        log.info("creating user {}", payload.getUsername());

        User user = User
                .builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .userProfile(Profile
                        .builder()
                        .displayName(payload.getName())
                        .build())
                .build();

        try {
            userService.registerUser(user, Role.USER);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true,"User registered successfully"));
    }
}
