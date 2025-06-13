package com.groupformer.controller;

import com.groupformer.dto.UserDto;
import com.groupformer.mapper.UserMapper;
import com.groupformer.model.User;
import com.groupformer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        if (userService.emailExists(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists: " + userDto.getEmail());
        }

        try {
            User user = UserMapper.toEntity(userDto);
            User savedUser = userService.createUser(user);
            UserDto responseDto = UserMapper.toDto(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                UserDto responseDto = UserMapper.toDto(user.get());
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching user: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDto> responseDtos = UserMapper.toDtoList(users);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching users: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            User user = UserMapper.toEntity(userDto);
            User updatedUser = userService.updateUser(id, user);
            UserDto responseDto = UserMapper.toDto(updatedUser);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok("User deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                UserDto responseDto = UserMapper.toDto(user.get());
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching user: " + e.getMessage());
        }
    }
    @PutMapping("/{id}/accept-cgu")
    public ResponseEntity<?> acceptCgu(@PathVariable Long id) {
        try {
            User updatedUser = userService.acceptCgu(id);
            UserDto responseDto = UserMapper.toDto(updatedUser);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error accepting CGU: " + e.getMessage());
        }
    }
}
