package com.groupformer.mapper;

import com.groupformer.dto.GroupDto;
import com.groupformer.model.Group;

import java.util.List;
import java.util.stream.Collectors;

public class GroupMapper {

    public static Group toEntity(GroupDto dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        return group;
    }

    public static GroupDto toDto(Group entity) {
        return new GroupDto(entity);
    }

    public static List<GroupDto> toDtoList(List<Group> entities) {
        return entities.stream()
                .map(GroupDto::new)
                .collect(Collectors.toList());
    }
}