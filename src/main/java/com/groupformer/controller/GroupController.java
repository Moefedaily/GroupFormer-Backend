package com.groupformer.controller;

import com.groupformer.dto.GroupDto;
import com.groupformer.mapper.GroupMapper;
import com.groupformer.model.Group;
import com.groupformer.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/groupdraw/{groupDrawId}")
    public ResponseEntity<?> createGroup(@PathVariable Long groupDrawId,
                                         @Valid @RequestBody GroupDto groupDto,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            Group group = GroupMapper.toEntity(groupDto);
            Group savedGroup = groupService.createGroup(group, groupDrawId);
            GroupDto responseDto = GroupMapper.toDto(savedGroup);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating group: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable Long id) {
        try {
            Optional<Group> group = groupService.getGroupById(id);
            if (group.isPresent()) {
                GroupDto responseDto = GroupMapper.toDto(group.get());
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching group: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        try {
            List<Group> groups = groupService.getAllGroups();
            List<GroupDto> responseDtos = GroupMapper.toDtoList(groups);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching groups: " + e.getMessage());
        }
    }

    @GetMapping("/groupdraw/{groupDrawId}")
    public ResponseEntity<?> getGroupsByGroupDrawId(@PathVariable Long groupDrawId) {
        try {
            List<Group> groups = groupService.getGroupsByGroupDrawId(groupDrawId);
            List<GroupDto> responseDtos = GroupMapper.toDtoList(groups);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching groups: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Long id,
                                         @Valid @RequestBody GroupDto groupDto,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            Group group = GroupMapper.toEntity(groupDto);
            Group updatedGroup = groupService.updateGroup(id, group);
            GroupDto responseDto = GroupMapper.toDto(updatedGroup);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating group: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        try {
            boolean deleted = groupService.deleteGroup(id);
            if (deleted) {
                return ResponseEntity.ok("Group deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting group: " + e.getMessage());
        }
    }

    @PostMapping("/{groupId}/persons/{personId}")
    public ResponseEntity<?> addPersonToGroup(@PathVariable Long groupId, @PathVariable Long personId) {
        try {
            Group updatedGroup = groupService.addPersonToGroup(groupId, personId);
            GroupDto responseDto = GroupMapper.toDto(updatedGroup);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding person to group: " + e.getMessage());
        }
    }

    @DeleteMapping("/{groupId}/persons/{personId}")
    public ResponseEntity<?> removePersonFromGroup(@PathVariable Long groupId, @PathVariable Long personId) {
        try {
            Group updatedGroup = groupService.removePersonFromGroup(groupId, personId);
            GroupDto responseDto = GroupMapper.toDto(updatedGroup);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing person from group: " + e.getMessage());
        }
    }
}