package com.groupformer.controller;

import com.groupformer.dto.GenerateGroupsRequest;
import com.groupformer.dto.GroupDrawDto;
import com.groupformer.mapper.GroupDrawMapper;
import com.groupformer.model.GroupDraw;
import com.groupformer.service.GroupGenerationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate-groups")
@CrossOrigin(origins = "*")
public class GroupGenerationController {

    @Autowired
    private GroupGenerationService groupGenerationService;

    @PostMapping
    public ResponseEntity<?> generateGroups(@Valid @RequestBody GenerateGroupsRequest request,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            GroupDraw groupDraw = groupGenerationService.generateGroups(request);
            GroupDrawDto responseDto = GroupDrawMapper.toDto(groupDraw);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating groups: " + e.getMessage());
        }
    }
}