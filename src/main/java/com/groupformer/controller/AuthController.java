package com.groupformer.controller;

import com.groupformer.dto.*;
import com.groupformer.dto.auth.LoginRequest;
import com.groupformer.dto.auth.RegisterRequest;
import com.groupformer.dto.email.AuthResponse;
import com.groupformer.dto.email.ResendVerificationRequest;
import com.groupformer.mapper.UserMapper;
import com.groupformer.model.User;
import com.groupformer.security.CustomUserDetails;
import com.groupformer.service.UserService;
import com.groupformer.service.email.EmailService;
import com.groupformer.util.Argon2PasswordEncoder;
import com.groupformer.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Argon2PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            String jwt = jwtUtil.generateVerifiedToken(
                    user.getEmail(),
                    user.getRole().name(),
                    user.getId()
            );

            AuthResponse response = new AuthResponse(
                    jwt,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name()
            );

            response.setEmailVerified(true);
            response.setMessage("Email verified successfully!");
            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            return ResponseEntity.badRequest().body("Email verification required. Please check your email and verify your account.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        if (userService.emailExists(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists: " + registerRequest.getEmail());
        }

        try {
            UserDto userDto = new UserDto();
            userDto.setName(registerRequest.getName());
            userDto.setEmail(registerRequest.getEmail());
            userDto.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userDto.setAddress(registerRequest.getAddress());
            userDto.setRole(User.UserRole.TRAINER.name());
            userDto.setCguAcceptedAt(LocalDateTime.now());

            User user = UserMapper.toEntity(userDto);
            User savedUser = userService.createUser(user);

            String unverifiedJwt = jwtUtil.generateUnverifiedToken(
                    savedUser.getEmail(),
                    savedUser.getRole().name(),
                    savedUser.getId()
            );

            String verificationToken = jwtUtil.generateEmailVerificationToken(
                    savedUser.getEmail(),
                    savedUser.getId()
            );

            emailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getName(),
                    verificationToken
            );

            AuthResponse response = new AuthResponse(
                    unverifiedJwt,
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getRole().name()
            );

            response.setEmailVerified(false);
            response.setMessage("Registration successful! Please check your email to verify your account.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {
        try {
            if (!jwtUtil.validateEmailVerificationToken(token)) {
                return ResponseEntity.badRequest().body("Invalid or expired verification token");
            }

            String email = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);

            Optional<User> userOptional = userService.getUserByEmail(email);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOptional.get();
            userService.markEmailAsVerified(userId);

            String verifiedJwt = jwtUtil.generateVerifiedToken(
                    user.getEmail(),
                    user.getRole().name(),
                    user.getId()
            );

            AuthResponse response = new AuthResponse(
                    verifiedJwt,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name()
            );

            response.setEmailVerified(true);
            response.setMessage("Email verified successfully! You can now access all features.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email verification failed: " + e.getMessage());
        }
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerificationEmail(@RequestBody ResendVerificationRequest request) {
        try {
            Optional<User> userOptional = userService.getUserByEmail(request.getEmail());
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOptional.get();

            String verificationToken = jwtUtil.generateEmailVerificationToken(
                    user.getEmail(),
                    user.getId()
            );

            emailService.sendVerificationEmail(
                    user.getEmail(),
                    user.getName(),
                    verificationToken
            );;

            return ResponseEntity.ok("Verification email sent! Please check your email.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend verification email: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}