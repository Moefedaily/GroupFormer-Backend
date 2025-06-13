package com.groupformer.mapper;

import com.groupformer.dto.StudentListDto;
import com.groupformer.model.StudentList;

import java.util.List;
import java.util.stream.Collectors;

public class StudentListMapper {

    public static StudentList toEntity(StudentListDto dto) {
        StudentList studentList = new StudentList();
        studentList.setId(dto.getId());
        studentList.setName(dto.getName());
        studentList.setCreatedAt(dto.getCreatedAt());
        return studentList;
    }

    public static StudentListDto toDto(StudentList entity) {
        return new StudentListDto(entity);
    }

    public static List<StudentListDto> toDtoList(List<StudentList> entities) {
        return entities.stream()
                .map(StudentListDto::new)
                .collect(Collectors.toList());
    }
}