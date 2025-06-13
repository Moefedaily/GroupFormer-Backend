package com.groupformer.mapper;

import com.groupformer.dto.GroupDrawDto;
import com.groupformer.model.GroupDraw;

import java.util.List;
import java.util.stream.Collectors;

public class GroupDrawMapper {

    public static GroupDraw toEntity(GroupDrawDto dto) {
        GroupDraw groupDraw = new GroupDraw();
        groupDraw.setId(dto.getId());
        groupDraw.setCreatedAt(dto.getCreatedAt());
        return groupDraw;
    }

    public static GroupDrawDto toDto(GroupDraw entity) {
        return new GroupDrawDto(entity);
    }

    public static List<GroupDrawDto> toDtoList(List<GroupDraw> entities) {
        return entities.stream()
                .map(GroupDrawDto::new)
                .collect(Collectors.toList());
    }
}