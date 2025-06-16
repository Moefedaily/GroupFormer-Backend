package com.groupformer.controller;

import com.groupformer.dto.*;
import com.groupformer.dto.Auth.AuthResponse;
import com.groupformer.dto.Auth.LoginRequest;
import com.groupformer.dto.Auth.RegisterRequest;
import com.groupformer.mapper.UserMapper;
import com.groupformer.model.User;
import com.groupformer.security.CustomUserDetails;
import com.groupformer.service.UserService;
import com.groupformer.util.Argon2PasswordEncoder;
import com.groupformer.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

            String jwt = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name(),
                    user.getId()
            );

            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name()
            ));

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
            String jwt = jwtUtil.generateToken(
                    savedUser.getEmail(),
                    savedUser.getRole().name(),
                    savedUser.getId()
            );

            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getRole().name()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}