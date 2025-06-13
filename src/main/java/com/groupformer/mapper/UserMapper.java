package com.groupformer.mapper;

import com.groupformer.dto.UserDto;
import com.groupformer.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(User.UserRole.valueOf(dto.getRole()));
        user.setAddress(dto.getAddress());
        user.setCreatedAt(dto.getCreatedAt());
        user.setCguAcceptedAt(dto.getCguAcceptedAt());
        return user;
    }

    public static UserDto toDto(User entity) {
        return new UserDto(entity);
    }

    public static List<UserDto> toDtoList(List<User> entities) {
        return entities.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }
}