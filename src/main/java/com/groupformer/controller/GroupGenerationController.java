package com.groupformer.controller;

import com.groupformer.dto.GenerateGroupsRequest;
import com.groupformer.dto.GroupDrawDto;
import com.groupformer.mapper.GroupDrawMapper;
import com.groupformer.model.GroupDraw;
import com.groupformer.model.StudentList;
import com.groupformer.security.CustomUserDetails;
import com.groupformer.service.GroupGenerationService;
import com.groupformer.service.StudentListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/generate-groups")
@CrossOrigin(origins = "*")
public class GroupGenerationController {

    @Autowired
    private GroupGenerationService groupGenerationService;

    @Autowired
    private StudentListService studentListService;

    private Long getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser().getId();
    }

    @PostMapping
    public ResponseEntity<?> generateGroups(@Valid @RequestBody GenerateGroupsRequest request,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            Long currentUserId = getCurrentUserId();

            Optional<StudentList> studentList = studentListService.getStudentListById(request.getStudentListId());
            if (!studentList.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            if (!studentList.get().getUser().getId().equals(currentUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only generate groups for your own lists");
            }

            GroupDraw groupDraw = groupGenerationService.generateGroups(request);
            GroupDrawDto responseDto = GroupDrawMapper.toDto(groupDraw);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating groups: " + e.getMessage());
        }
    }
}