package com.groupformer.controller;

import com.groupformer.dto.GroupDrawDto;
import com.groupformer.mapper.GroupDrawMapper;
import com.groupformer.model.GroupDraw;
import com.groupformer.service.GroupDrawService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groupdraws")
@CrossOrigin(origins = "*")
public class GroupDrawController {

    @Autowired
    private GroupDrawService groupDrawService;

    @PostMapping("/studentlist/{studentListId}")
    public ResponseEntity<?> createGroupDraw(@PathVariable Long studentListId,
                                             @Valid @RequestBody GroupDrawDto groupDrawDto,
                                             BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            GroupDraw groupDraw = GroupDrawMapper.toEntity(groupDrawDto);
            GroupDraw savedGroupDraw = groupDrawService.createGroupDraw(groupDraw, studentListId);
            GroupDrawDto responseDto = GroupDrawMapper.toDto(savedGroupDraw);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating group draw: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupDrawById(@PathVariable Long id) {
        try {
            Optional<GroupDraw> groupDraw = groupDrawService.getGroupDrawById(id);
            if (groupDraw.isPresent()) {
                GroupDrawDto responseDto = GroupDrawMapper.toDto(groupDraw.get());
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching group draw: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGroupDraws() {
        try {
            List<GroupDraw> groupDraws = groupDrawService.getAllGroupDraws();
            List<GroupDrawDto> responseDtos = GroupDrawMapper.toDtoList(groupDraws);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching group draws: " + e.getMessage());
        }
    }

    @GetMapping("/studentlist/{studentListId}")
    public ResponseEntity<?> getGroupDrawsByStudentListId(@PathVariable Long studentListId) {
        try {
            List<GroupDraw> groupDraws = groupDrawService.getGroupDrawsByStudentListId(studentListId);
            List<GroupDrawDto> responseDtos = GroupDrawMapper.toDtoList(groupDraws);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching group draws: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroupDraw(@PathVariable Long id) {
        try {
            boolean deleted = groupDrawService.deleteGroupDraw(id);
            if (deleted) {
                return ResponseEntity.ok("Group draw deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting group draw: " + e.getMessage());
        }
    }
}