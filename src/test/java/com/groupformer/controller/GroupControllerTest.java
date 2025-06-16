package com.groupformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupformer.model.Group;
import com.groupformer.model.GroupDraw;
import com.groupformer.model.Person;
import com.groupformer.dto.GroupDto;
import com.groupformer.service.GroupService;
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

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    private Group testGroup;
    private GroupDraw testGroupDraw;
    private Person testPerson;

    @BeforeEach
    void setUp() {
        testGroupDraw = new GroupDraw();
        testGroupDraw.setId(1L);
        testGroupDraw.setCreatedAt(LocalDateTime.now());

        testPerson = new Person();
        testPerson.setId(1L);
        testPerson.setName("Neymar Jr");
        testPerson.setGender(Person.Gender.MALE);
        testPerson.setAge(32);
        testPerson.setPersonalityProfile(Person.PersonalityProfile.COMFORTABLE);
        testPerson.setFrenchLevel(3);
        testPerson.setTechnicalLevel(4);
        testPerson.setFormerDwwm(false);

        testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("Barcelona Squad");
        testGroup.setGroupDraw(testGroupDraw);
        testGroup.setPersons(Arrays.asList(testPerson));
    }

    @Test
    @DisplayName("Should create group successfully")
    void shouldCreateGroupSuccessfully() throws Exception {
        GroupDto requestDto = new GroupDto();
        requestDto.setName("Barcelona Squad");
        requestDto.setGroupDrawId(1L);

        when(groupService.createGroup(any(Group.class), anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/api/groups/groupdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Barcelona Squad"))
                .andExpect(jsonPath("$.groupDrawId").value(1))
                .andExpect(jsonPath("$.personCount").value(1));

        verify(groupService).createGroup(any(Group.class), eq(1L));
    }

    @Test
    @DisplayName("Should get group by ID successfully")
    void shouldGetGroupByIdSuccessfully() throws Exception {
        when(groupService.getGroupById(1L)).thenReturn(Optional.of(testGroup));

        mockMvc.perform(get("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Barcelona Squad"))
                .andExpect(jsonPath("$.personCount").value(1));

        verify(groupService).getGroupById(1L);
    }

    @Test
    @DisplayName("Should get all groups successfully")
    void shouldGetAllGroupsSuccessfully() throws Exception {
        List<Group> groups = Arrays.asList(testGroup);
        when(groupService.getAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Barcelona Squad"));

        verify(groupService).getAllGroups();
    }

    @Test
    @DisplayName("Should get groups by group draw successfully")
    void shouldGetGroupsByGroupDrawSuccessfully() throws Exception {
        List<Group> groups = Arrays.asList(testGroup);
        when(groupService.getGroupsByGroupDrawId(1L)).thenReturn(groups);

        mockMvc.perform(get("/api/groups/groupdraw/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Barcelona Squad"))
                .andExpect(jsonPath("$[0].groupDrawId").value(1));

        verify(groupService).getGroupsByGroupDrawId(1L);
    }

    @Test
    @DisplayName("Should update group successfully")
    void shouldUpdateGroupSuccessfully() throws Exception {
        GroupDto updateDto = new GroupDto();
        updateDto.setName("Real Madrid Squad");

        Group updatedGroup = new Group();
        updatedGroup.setId(1L);
        updatedGroup.setName("Real Madrid Squad");
        updatedGroup.setGroupDraw(testGroupDraw);

        when(groupService.updateGroup(eq(1L), any(Group.class))).thenReturn(updatedGroup);

        mockMvc.perform(put("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Real Madrid Squad"));

        verify(groupService).updateGroup(eq(1L), any(Group.class));
    }

    @Test
    @DisplayName("Should delete group successfully")
    void shouldDeleteGroupSuccessfully() throws Exception {
        when(groupService.deleteGroup(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Group deleted successfully"));

        verify(groupService).deleteGroup(1L);
    }

    @Test
    @DisplayName("Should add person to group successfully")
    void shouldAddPersonToGroupSuccessfully() throws Exception {
        when(groupService.addPersonToGroup(1L, 1L)).thenReturn(testGroup);

        mockMvc.perform(post("/api/groups/1/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Barcelona Squad"))
                .andExpect(jsonPath("$.personCount").value(1));

        verify(groupService).addPersonToGroup(1L, 1L);
    }

    @Test
    @DisplayName("Should remove person from group successfully")
    void shouldRemovePersonFromGroupSuccessfully() throws Exception {
        Group emptyGroup = new Group();
        emptyGroup.setId(1L);
        emptyGroup.setName("Barcelona Squad");
        emptyGroup.setGroupDraw(testGroupDraw);
        emptyGroup.setPersons(Arrays.asList());

        when(groupService.removePersonFromGroup(1L, 1L)).thenReturn(emptyGroup);

        mockMvc.perform(delete("/api/groups/1/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.personCount").value(0));

        verify(groupService).removePersonFromGroup(1L, 1L);
    }
}