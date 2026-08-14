package com.resumeiq.backend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resumeiq.backend.constant.ApiMessages;
import com.resumeiq.backend.entity.Role;
import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.entity.enums.AuthProvider;
import com.resumeiq.backend.entity.enums.RoleType;
import com.resumeiq.backend.exception.DuplicateResourceException;
import com.resumeiq.backend.exception.ResourceNotFoundException;
import com.resumeiq.backend.repository.RoleRepository;
import com.resumeiq.backend.repository.UserRepository;
import com.resumeiq.backend.request.LoginRequest;
import com.resumeiq.backend.request.RegisterRequest;
import com.resumeiq.backend.response.AuthenticationResponse;
import com.resumeiq.backend.security.CustomUserDetailsService;
import com.resumeiq.backend.security.JwtService;
import com.resumeiq.backend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService customUserDetailsService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    // ====================================
    // Register
    // ====================================

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        validateDuplicateEmail(request.getEmail());

        log.info("Register request received for {}", request.getEmail());

        Role role = getDefaultUserRole();

        User user = buildUser(request, role);

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getEmail());

        return buildAuthenticationResponse(user.getEmail());
    }

    // ====================================
    // Login
    // ====================================

   @Override
public AuthenticationResponse login(
        LoginRequest request
) {

    String email =
            request.getEmail()
                    .trim()
                    .toLowerCase();

    log.info(
            "Login request received for {}",
            email
    );

    findUser(email);

    authenticate(
            email,
            request.getPassword()
    );

    log.info(
            "Login successful: {}",
            email
    );

    return buildAuthenticationResponse(
            email
    );
}
    // ====================================
    // Helper Methods
    // ====================================

    private void validateDuplicateEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    ApiMessages.EMAIL_ALREADY_EXISTS);
        }
    }

    private Role getDefaultUserRole() {

        return roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ApiMessages.ROLE_NOT_FOUND));
    }

    private User findUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private void authenticate(String email, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password));
    }

    private AuthenticationResponse buildAuthenticationResponse(String email) {

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(email);

        String token = jwtService.generateToken(userDetails);

        return new AuthenticationResponse(token);
    }

    private User buildUser(RegisterRequest request, Role role) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(role);
        user.setProvider(AuthProvider.LOCAL);

        user.setEnabled(true);
        user.setEmailVerified(false);

        return user;
    }
}