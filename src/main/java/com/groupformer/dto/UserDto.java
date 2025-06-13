package com.groupformer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupformer.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime cguAcceptedAt;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.address = user.getAddress();
        this.createdAt = user.getCreatedAt();
        this.cguAcceptedAt = user.getCguAcceptedAt();
    }
}