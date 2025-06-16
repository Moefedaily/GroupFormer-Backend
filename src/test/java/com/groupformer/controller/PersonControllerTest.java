package com.groupformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupformer.model.Person;
import com.groupformer.model.StudentList;
import com.groupformer.dto.PersonDto;
import com.groupformer.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person testPerson;
    private StudentList testStudentList;

    @BeforeEach
    void setUp() {
        testStudentList = new StudentList();
        testStudentList.setId(1L);
        testStudentList.setName("Football Academy");

        testPerson = new Person();
        testPerson.setId(1L);
        testPerson.setName("Cristiano Ronaldo");
        testPerson.setGender(Person.Gender.MALE);
        testPerson.setAge(39);
        testPerson.setFrenchLevel(2);
        testPerson.setTechnicalLevel(4);
        testPerson.setFormerDwwm(false);
        testPerson.setPersonalityProfile(Person.PersonalityProfile.COMFORTABLE);
        testPerson.setStudentList(testStudentList);
    }

    @Test
    @DisplayName("Should create person successfully")
    void shouldCreatePersonSuccessfully() throws Exception {
        PersonDto requestDto = new PersonDto();
        requestDto.setName("Cristiano Ronaldo");
        requestDto.setGender("MALE");
        requestDto.setAge(39);
        requestDto.setFrenchLevel(2);
        requestDto.setTechnicalLevel(4);
        requestDto.setFormerDwwm(false);
        requestDto.setPersonalityProfile("COMFORTABLE");

        when(personService.personNameExistsInList(anyString(), anyLong())).thenReturn(false);
        when(personService.createPerson(any(Person.class), anyLong())).thenReturn(testPerson);

        mockMvc.perform(post("/api/persons/studentlist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cristiano Ronaldo"))
                .andExpect(jsonPath("$.gender").value("masculin"))
                .andExpect(jsonPath("$.age").value(39))
                .andExpect(jsonPath("$.technicalLevel").value(4));

        verify(personService).personNameExistsInList("Cristiano Ronaldo", 1L);
        verify(personService).createPerson(any(Person.class), eq(1L));
    }

    @Test
    @DisplayName("Should get person by ID successfully")
    void shouldGetPersonByIdSuccessfully() throws Exception {
        when(personService.getPersonById(1L)).thenReturn(Optional.of(testPerson));

        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cristiano Ronaldo"))
                .andExpect(jsonPath("$.gender").value("masculin"))
                .andExpect(jsonPath("$.age").value(39));

        verify(personService).getPersonById(1L);
    }

    @Test
    @DisplayName("Should get all persons successfully")
    void shouldGetAllPersonsSuccessfully() throws Exception {
        List<Person> persons = Arrays.asList(testPerson);
        when(personService.getAllPersons()).thenReturn(persons);

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cristiano Ronaldo"))
                .andExpect(jsonPath("$[0].technicalLevel").value(4));

        verify(personService).getAllPersons();
    }

    @Test
    @DisplayName("Should get persons by student list successfully")
    void shouldGetPersonsByStudentListSuccessfully() throws Exception {
        List<Person> persons = Arrays.asList(testPerson);
        when(personService.getPersonsByStudentListId(1L)).thenReturn(persons);

        mockMvc.perform(get("/api/persons/studentlist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cristiano Ronaldo"))
                .andExpect(jsonPath("$[0].studentListId").value(1));

        verify(personService).getPersonsByStudentListId(1L);
    }

    @Test
    @DisplayName("Should update person successfully")
    void shouldUpdatePersonSuccessfully() throws Exception {
        PersonDto updateDto = new PersonDto();
        updateDto.setName("Cristiano Updated");
        updateDto.setGender("MALE");
        updateDto.setAge(40);
        updateDto.setFrenchLevel(3);
        updateDto.setTechnicalLevel(4);
        updateDto.setFormerDwwm(true);
        updateDto.setPersonalityProfile("COMFORTABLE");

        Person updatedPerson = new Person();
        updatedPerson.setId(1L);
        updatedPerson.setName("Cristiano Updated");
        updatedPerson.setGender(Person.Gender.MALE);
        updatedPerson.setAge(40);
        updatedPerson.setFrenchLevel(3);
        updatedPerson.setTechnicalLevel(4);
        updatedPerson.setFormerDwwm(true);
        updatedPerson.setPersonalityProfile(Person.PersonalityProfile.COMFORTABLE);
        updatedPerson.setStudentList(testStudentList);

        when(personService.updatePerson(eq(1L), any(Person.class))).thenReturn(updatedPerson);

        mockMvc.perform(put("/api/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cristiano Updated"))
                .andExpect(jsonPath("$.age").value(40))
                .andExpect(jsonPath("$.frenchLevel").value(3));

        verify(personService).updatePerson(eq(1L), any(Person.class));
    }

    @Test
    @DisplayName("Should delete person successfully")
    void shouldDeletePersonSuccessfully() throws Exception {
        when(personService.deletePerson(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person deleted successfully"));

        verify(personService).deletePerson(1L);
    }
}