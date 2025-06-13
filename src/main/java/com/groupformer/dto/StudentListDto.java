package com.groupformer.dto;

import com.groupformer.model.StudentList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentListDto {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
    private int personCount;
    private int groupDrawCount;

    public StudentListDto(StudentList studentList) {
        this.id = studentList.getId();
        this.name = studentList.getName();
        this.createdAt = studentList.getCreatedAt();
        this.userId = studentList.getUser().getId();
        this.userName = studentList.getUser().getName();
        this.personCount = studentList.getPersons() != null ? studentList.getPersons().size() : 0;
        this.groupDrawCount = studentList.getGroupDraws() != null ? studentList.getGroupDraws().size() : 0;
    }
}