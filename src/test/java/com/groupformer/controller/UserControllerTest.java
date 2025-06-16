package com.groupformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupformer.model.User;
import com.groupformer.dto.UserDto;
import com.groupformer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Wayne Rooney");
        testUser.setEmail("rooney.wayne@example.com");
        testUser.setRole(User.UserRole.TRAINER);
        testUser.setCreatedAt(LocalDateTime.now());

        UserDto testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("Wayne Rooney");
        testUserDto.setEmail("rooney.wayne@example.com");
        testUserDto.setPassword("password123");
        testUserDto.setRole("TRAINER");
        testUserDto.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setName("Wayne Rooney");
        requestDto.setEmail("rooney.wayne@example.com");
        requestDto.setPassword("password123");
        requestDto.setRole("TRAINER");

        when(userService.emailExists(anyString())).thenReturn(false);
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Wayne Rooney"))
                .andExpect(jsonPath("$.email").value("rooney.wayne@example.com"))
                .andExpect(jsonPath("$.role").value("TRAINER"));

        verify(userService).emailExists("rooney.wayne@example.com");
        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Wayne Rooney"))
                .andExpect(jsonPath("$.email").value("rooney.wayne@example.com"))
                .andExpect(jsonPath("$.role").value("TRAINER"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Wayne Rooney"))
                .andExpect(jsonPath("$[0].email").value("rooney.wayne@example.com"));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() throws Exception {
        UserDto updateDto = new UserDto();
        updateDto.setName("Rooney Updated");
        updateDto.setEmail("rooney.wayne@example.com");
        updateDto.setRole("TRAINER");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Rooney Updated");
        updatedUser.setEmail("rooney.wayne@example.com");
        updatedUser.setRole(User.UserRole.TRAINER);

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rooney Updated"))
                .andExpect(jsonPath("$.email").value("rooney.wayne@example.com"));

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Should get user by email successfully")
    void shouldGetUserByEmailSuccessfully() throws Exception {
        when(userService.getUserByEmail("rooney.wayne@example.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/email/rooney.wayne@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Wayne Rooney"))
                .andExpect(jsonPath("$.email").value("rooney.wayne@example.com"));

        verify(userService).getUserByEmail("rooney.wayne@example.com");
    }

    @Test
    @DisplayName("Should accept CGU successfully")
    void shouldAcceptCguSuccessfully() throws Exception {
        // Given
        User userWithCgu = new User();
        userWithCgu.setId(1L);
        userWithCgu.setName("Wayne Rooney");
        userWithCgu.setEmail("rooney.wayne@example.com");
        userWithCgu.setRole(User.UserRole.TRAINER);
        userWithCgu.setCguAcceptedAt(LocalDateTime.now());

        when(userService.acceptCgu(1L)).thenReturn(userWithCgu);
        mockMvc.perform(put("/api/users/1/accept-cgu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cguAcceptedAt").exists());

        verify(userService).acceptCgu(1L);
    }
}