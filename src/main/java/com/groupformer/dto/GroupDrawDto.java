package com.groupformer.dto;

import com.groupformer.model.GroupDraw;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDrawDto {

    private Long id;
    private LocalDateTime createdAt;
    private Long studentListId;
    private String studentListName;
    private int groupCount;

    public GroupDrawDto(GroupDraw groupDraw) {
        this.id = groupDraw.getId();
        this.createdAt = groupDraw.getCreatedAt();
        this.studentListId = groupDraw.getStudentList().getId();
        this.studentListName = groupDraw.getStudentList().getName();
        this.groupCount = groupDraw.getGroups() != null ? groupDraw.getGroups().size() : 0;
    }
}