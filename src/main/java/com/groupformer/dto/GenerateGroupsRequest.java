package com.groupformer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateGroupsRequest {

    @NotNull(message = "Student list ID is required")
    private Long studentListId;

    @Min(value = 2, message = "At least 2 groups are required")
    private Integer numberOfGroups;

    @NotEmpty(message = "Group names are required")
    private List<String> groupNames;

    private Boolean mixGender = false;
    private Boolean mixAge = false;
    private Boolean mixFrenchLevel = false;
    private Boolean mixTechnicalLevel = false;
    private Boolean mixFormerDwwm = false;
    private Boolean mixPersonalityProfile = false;
}