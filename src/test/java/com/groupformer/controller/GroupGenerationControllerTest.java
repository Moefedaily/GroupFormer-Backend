package com.groupformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupformer.model.GroupDraw;
import com.groupformer.model.StudentList;
import com.groupformer.model.User;
import com.groupformer.dto.GenerateGroupsRequest;
import com.groupformer.service.GroupGenerationService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupGenerationController.class)
class GroupGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupGenerationService groupGenerationService;

    @Autowired
    private ObjectMapper objectMapper;

    private GroupDraw testGroupDraw;
    private GenerateGroupsRequest testRequest;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Zinedine Zidane");
        testUser.setEmail("zidane.zinedine@example.com");

        StudentList testStudentList = new StudentList();
        testStudentList.setId(1L);
        testStudentList.setName("World Cup Academy");
        testStudentList.setUser(testUser);

        testGroupDraw = new GroupDraw();
        testGroupDraw.setId(1L);
        testGroupDraw.setStudentList(testStudentList);
        testGroupDraw.setCreatedAt(LocalDateTime.now());

        testRequest = new GenerateGroupsRequest();
        testRequest.setStudentListId(1L);
        testRequest.setNumberOfGroups(2);
        testRequest.setGroupNames(Arrays.asList("Team France", "Team Brazil"));
        testRequest.setMixGender(true);
        testRequest.setMixAge(false);
        testRequest.setMixFrenchLevel(false);
        testRequest.setMixTechnicalLevel(true);
        testRequest.setMixFormerDwwm(false);
        testRequest.setMixPersonalityProfile(false);
    }

    @Test
    @DisplayName("Should generate groups successfully")
    void shouldGenerateGroupsSuccessfully() throws Exception {
        when(groupGenerationService.generateGroups(any(GenerateGroupsRequest.class))).thenReturn(testGroupDraw);

        mockMvc.perform(post("/api/generate-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentListId").value(1))
                .andExpect(jsonPath("$.studentListName").value("World Cup Academy"));

        verify(groupGenerationService).generateGroups(any(GenerateGroupsRequest.class));
    }

    @Test
    @DisplayName("Should generate groups with all criteria successfully")
    void shouldGenerateGroupsWithAllCriteriaSuccessfully() throws Exception {
        GenerateGroupsRequest complexRequest = new GenerateGroupsRequest();
        complexRequest.setStudentListId(1L);
        complexRequest.setNumberOfGroups(3);
        complexRequest.setGroupNames(Arrays.asList("Team A", "Team B", "Team C"));
        complexRequest.setMixGender(true);
        complexRequest.setMixAge(true);
        complexRequest.setMixFrenchLevel(true);
        complexRequest.setMixTechnicalLevel(true);
        complexRequest.setMixFormerDwwm(true);
        complexRequest.setMixPersonalityProfile(true);

        when(groupGenerationService.generateGroups(any(GenerateGroupsRequest.class))).thenReturn(testGroupDraw);

        mockMvc.perform(post("/api/generate-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complexRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentListId").value(1));

        verify(groupGenerationService).generateGroups(any(GenerateGroupsRequest.class));
    }

    @Test
    @DisplayName("Should generate groups with minimum criteria successfully")
    void shouldGenerateGroupsWithMinimumCriteriaSuccessfully() throws Exception {
        GenerateGroupsRequest minimalRequest = new GenerateGroupsRequest();
        minimalRequest.setStudentListId(1L);
        minimalRequest.setNumberOfGroups(2);
        minimalRequest.setGroupNames(Arrays.asList("Group 1", "Group 2"));
        minimalRequest.setMixGender(false);
        minimalRequest.setMixAge(false);
        minimalRequest.setMixFrenchLevel(false);
        minimalRequest.setMixTechnicalLevel(false);
        minimalRequest.setMixFormerDwwm(false);
        minimalRequest.setMixPersonalityProfile(false);

        when(groupGenerationService.generateGroups(any(GenerateGroupsRequest.class))).thenReturn(testGroupDraw);

        mockMvc.perform(post("/api/generate-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(groupGenerationService).generateGroups(any(GenerateGroupsRequest.class));
    }

    @Test
    @DisplayName("Should validate group generation with large number of groups")
    void shouldValidateGroupGenerationWithLargeNumberOfGroups() throws Exception {
        GenerateGroupsRequest largeRequest = new GenerateGroupsRequest();
        largeRequest.setStudentListId(1L);
        largeRequest.setNumberOfGroups(5);
        largeRequest.setGroupNames(Arrays.asList("Team 1", "Team 2", "Team 3", "Team 4", "Team 5"));
        largeRequest.setMixGender(true);
        largeRequest.setMixTechnicalLevel(true);

        when(groupGenerationService.generateGroups(any(GenerateGroupsRequest.class))).thenReturn(testGroupDraw);

        mockMvc.perform(post("/api/generate-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(largeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(groupGenerationService).generateGroups(any(GenerateGroupsRequest.class));
    }

    @Test
    @DisplayName("Should generate groups with custom group names successfully")
    void shouldGenerateGroupsWithCustomGroupNamesSuccessfully() throws Exception {
        GenerateGroupsRequest customRequest = new GenerateGroupsRequest();
        customRequest.setStudentListId(1L);
        customRequest.setNumberOfGroups(4);
        customRequest.setGroupNames(Arrays.asList("Barcelona", "Real Madrid", "PSG", "Manchester City"));
        customRequest.setMixGender(true);
        customRequest.setMixAge(true);

        when(groupGenerationService.generateGroups(any(GenerateGroupsRequest.class))).thenReturn(testGroupDraw);

        mockMvc.perform(post("/api/generate-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(groupGenerationService).generateGroups(any(GenerateGroupsRequest.class));
    }
}