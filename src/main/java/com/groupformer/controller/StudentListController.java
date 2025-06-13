package com.groupformer.controller;

import com.groupformer.dto.StudentListDto;
import com.groupformer.mapper.StudentListMapper;
import com.groupformer.model.StudentList;
import com.groupformer.service.StudentListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/studentlists")
@CrossOrigin(origins = "*")
public class StudentListController {

    @Autowired
    private StudentListService studentListService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createStudentList(@PathVariable Long userId,
                                               @Valid @RequestBody StudentListDto studentListDto,
                                               BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        if (studentListService.listNameExistsForUser(studentListDto.getName(), userId)) {
            return ResponseEntity.badRequest().body("List name already exists for this user: " + studentListDto.getName());
        }

        try {
            StudentList studentList = StudentListMapper.toEntity(studentListDto);
            StudentList savedList = studentListService.createStudentList(studentList, userId);
            StudentListDto responseDto = StudentListMapper.toDto(savedList);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating student list: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentListById(@PathVariable Long id) {
        try {
            Optional<StudentList> studentList = studentListService.getStudentListById(id);
            if (studentList.isPresent()) {
                StudentListDto responseDto = StudentListMapper.toDto(studentList.get());
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching student list: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllStudentLists() {
        try {
            List<StudentList> studentLists = studentListService.getAllStudentLists();
            List<StudentListDto> responseDtos = StudentListMapper.toDtoList(studentLists);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching student lists: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getStudentListsByUserId(@PathVariable Long userId) {
        try {
            List<StudentList> studentLists = studentListService.getStudentListsByUserId(userId);
            List<StudentListDto> responseDtos = StudentListMapper.toDtoList(studentLists);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching student lists: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentList(@PathVariable Long id,
                                               @Valid @RequestBody StudentListDto studentListDto,
                                               BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            StudentList studentList = StudentListMapper.toEntity(studentListDto);
            StudentList updatedList = studentListService.updateStudentList(id, studentList);
            StudentListDto responseDto = StudentListMapper.toDto(updatedList);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating student list: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentList(@PathVariable Long id) {
        try {
            boolean deleted = studentListService.deleteStudentList(id);
            if (deleted) {
                return ResponseEntity.ok("Student list deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting student list: " + e.getMessage());
        }
    }
}