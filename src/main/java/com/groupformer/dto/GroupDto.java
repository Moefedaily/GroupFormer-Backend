package com.groupformer.dto;

import com.groupformer.model.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    private Long id;
    private String name;
    private Long groupDrawId;
    private List<PersonDto> persons;
    private int personCount;

    public GroupDto(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.groupDrawId = group.getGroupDraw().getId();
        this.persons = group.getPersons() != null ?
                group.getPersons().stream()
                        .map(PersonDto::new)
                        .collect(Collectors.toList()) :
                List.of();
        this.personCount = this.persons.size();
    }
}