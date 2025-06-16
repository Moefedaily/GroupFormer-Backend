package com.groupformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupformer.model.StudentList;
import com.groupformer.model.User;
import com.groupformer.dto.StudentListDto;
import com.groupformer.service.StudentListService;
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

@WebMvcTest(StudentListController.class)
class StudentListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentListService studentListService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentList testStudentList;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Lionel Messi");
        testUser.setEmail("messi.lionel@example.com");
        testUser.setRole(User.UserRole.TRAINER);

        testStudentList = new StudentList();
        testStudentList.setId(1L);
        testStudentList.setName("Advanced JavaScript Class");
        testStudentList.setUser(testUser);
        testStudentList.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create student list successfully")
    void shouldCreateStudentListSuccessfully() throws Exception {
        StudentListDto requestDto = new StudentListDto();
        requestDto.setName("Advanced JavaScript Class");

        when(studentListService.listNameExistsForUser(anyString(), anyLong())).thenReturn(false);
        when(studentListService.createStudentList(any(StudentList.class), anyLong())).thenReturn(testStudentList);

        mockMvc.perform(post("/api/studentlists/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Advanced JavaScript Class"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("Lionel Messi"));

        verify(studentListService).listNameExistsForUser("Advanced JavaScript Class", 1L);
        verify(studentListService).createStudentList(any(StudentList.class), eq(1L));
    }

    @Test
    @DisplayName("Should get student list by ID successfully")
    void shouldGetStudentListByIdSuccessfully() throws Exception {
        when(studentListService.getStudentListById(1L)).thenReturn(Optional.of(testStudentList));

        mockMvc.perform(get("/api/studentlists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Advanced JavaScript Class"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("Lionel Messi"));

        verify(studentListService).getStudentListById(1L);
    }

    @Test
    @DisplayName("Should get all student lists successfully")
    void shouldGetAllStudentListsSuccessfully() throws Exception {
        List<StudentList> studentLists = Arrays.asList(testStudentList);
        when(studentListService.getAllStudentLists()).thenReturn(studentLists);

        mockMvc.perform(get("/api/studentlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Advanced JavaScript Class"))
                .andExpect(jsonPath("$[0].userName").value("Lionel Messi"));

        verify(studentListService).getAllStudentLists();
    }

    @Test
    @DisplayName("Should get student lists by user successfully")
    void shouldGetStudentListsByUserSuccessfully() throws Exception {
        List<StudentList> studentLists = Arrays.asList(testStudentList);
        when(studentListService.getStudentListsByUserId(1L)).thenReturn(studentLists);

        mockMvc.perform(get("/api/studentlists/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Advanced JavaScript Class"))
                .andExpect(jsonPath("$[0].userId").value(1));

        verify(studentListService).getStudentListsByUserId(1L);
    }

    @Test
    @DisplayName("Should update student list successfully")
    void shouldUpdateStudentListSuccessfully() throws Exception {
        StudentListDto updateDto = new StudentListDto();
        updateDto.setName("Updated JavaScript Class");

        StudentList updatedStudentList = new StudentList();
        updatedStudentList.setId(1L);
        updatedStudentList.setName("Updated JavaScript Class");
        updatedStudentList.setUser(testUser);

        when(studentListService.updateStudentList(eq(1L), any(StudentList.class))).thenReturn(updatedStudentList);

        mockMvc.perform(put("/api/studentlists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated JavaScript Class"))
                .andExpect(jsonPath("$.userName").value("Lionel Messi"));

        verify(studentListService).updateStudentList(eq(1L), any(StudentList.class));
    }

    @Test
    @DisplayName("Should delete student list successfully")
    void shouldDeleteStudentListSuccessfully() throws Exception {
        when(studentListService.deleteStudentList(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/studentlists/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student list deleted successfully"));

        verify(studentListService).deleteStudentList(1L);
    }
}