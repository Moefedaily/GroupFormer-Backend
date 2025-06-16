package com.groupformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupformer.model.GroupDraw;
import com.groupformer.model.StudentList;
import com.groupformer.model.User;
import com.groupformer.dto.GroupDrawDto;
import com.groupformer.service.GroupDrawService;
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

@WebMvcTest(GroupDrawController.class)
class GroupDrawControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupDrawService groupDrawService;

    @Autowired
    private ObjectMapper objectMapper;

    private GroupDraw testGroupDraw;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Kylian Mbappe");
        testUser.setEmail("mbappe.kylian@example.com");

        StudentList testStudentList = new StudentList();
        testStudentList.setId(1L);
        testStudentList.setName("Champions League Class");
        testStudentList.setUser(testUser);

        testGroupDraw = new GroupDraw();
        testGroupDraw.setId(1L);
        testGroupDraw.setStudentList(testStudentList);
        testGroupDraw.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create group draw successfully")
    void shouldCreateGroupDrawSuccessfully() throws Exception {
        GroupDrawDto requestDto = new GroupDrawDto();
        requestDto.setStudentListId(1L);

        when(groupDrawService.createGroupDraw(any(GroupDraw.class), anyLong())).thenReturn(testGroupDraw);

        mockMvc.perform(post("/api/groupdraws/studentlist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentListId").value(1))
                .andExpect(jsonPath("$.studentListName").value("Champions League Class"));

        verify(groupDrawService).createGroupDraw(any(GroupDraw.class), eq(1L));
    }

    @Test
    @DisplayName("Should get group draw by ID successfully")
    void shouldGetGroupDrawByIdSuccessfully() throws Exception {
        when(groupDrawService.getGroupDrawById(1L)).thenReturn(Optional.of(testGroupDraw));

        mockMvc.perform(get("/api/groupdraws/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentListId").value(1))
                .andExpect(jsonPath("$.studentListName").value("Champions League Class"));

        verify(groupDrawService).getGroupDrawById(1L);
    }

    @Test
    @DisplayName("Should get all group draws successfully")
    void shouldGetAllGroupDrawsSuccessfully() throws Exception {
        List<GroupDraw> groupDraws = Arrays.asList(testGroupDraw);
        when(groupDrawService.getAllGroupDraws()).thenReturn(groupDraws);

        mockMvc.perform(get("/api/groupdraws"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].studentListId").value(1));

        verify(groupDrawService).getAllGroupDraws();
    }

    @Test
    @DisplayName("Should get group draws by student list successfully")
    void shouldGetGroupDrawsByStudentListSuccessfully() throws Exception {
        List<GroupDraw> groupDraws = Arrays.asList(testGroupDraw);
        when(groupDrawService.getGroupDrawsByStudentListId(1L)).thenReturn(groupDraws);

        mockMvc.perform(get("/api/groupdraws/studentlist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].studentListId").value(1))
                .andExpect(jsonPath("$[0].studentListName").value("Champions League Class"));

        verify(groupDrawService).getGroupDrawsByStudentListId(1L);
    }

    @Test
    @DisplayName("Should delete group draw successfully")
    void shouldDeleteGroupDrawSuccessfully() throws Exception {
        when(groupDrawService.deleteGroupDraw(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/groupdraws/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Group draw deleted successfully"));

        verify(groupDrawService).deleteGroupDraw(1L);
    }
}