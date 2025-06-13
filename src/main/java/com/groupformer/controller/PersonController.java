package com.groupformer.controller;

import com.groupformer.dto.PersonDto;
import com.groupformer.mapper.PersonMapper;
import com.groupformer.model.Person;
import com.groupformer.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
@CrossOrigin(origins = "*")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/studentlist/{studentListId}")
    public ResponseEntity<?> createPerson(@PathVariable Long studentListId,
                                          @Valid @RequestBody PersonDto personDto,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        if (personService.personNameExistsInList(personDto.getName(), studentListId)) {
            return ResponseEntity.badRequest().body("Person name already exists in this list: " + personDto.getName());
        }

        try {
            Person person = PersonMapper.toEntity(personDto);
            Person savedPerson = personService.createPerson(person, studentListId);
            PersonDto responseDto = PersonMapper.toDto(savedPerson);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating person: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        try {
            Optional<Person> person = personService.getPersonById(id);
            if (person.isPresent()) {
                PersonDto responseDto = PersonMapper.toDto(person.get());
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching person: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPersons() {
        try {
            List<Person> persons = personService.getAllPersons();
            List<PersonDto> responseDtos = PersonMapper.toDtoList(persons);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching persons: " + e.getMessage());
        }
    }

    @GetMapping("/studentlist/{studentListId}")
    public ResponseEntity<?> getPersonsByStudentListId(@PathVariable Long studentListId) {
        try {
            List<Person> persons = personService.getPersonsByStudentListId(studentListId);
            List<PersonDto> responseDtos = PersonMapper.toDtoList(persons);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching persons: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id,
                                          @Valid @RequestBody PersonDto personDto,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            Person person = PersonMapper.toEntity(personDto);
            Person updatedPerson = personService.updatePerson(id, person);
            PersonDto responseDto = PersonMapper.toDto(updatedPerson);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating person: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {
            boolean deleted = personService.deletePerson(id);
            if (deleted) {
                return ResponseEntity.ok("Person deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting person: " + e.getMessage());
        }
    }
}