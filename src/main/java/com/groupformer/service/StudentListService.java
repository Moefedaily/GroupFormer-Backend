package com.groupformer.service;

import com.groupformer.model.StudentList;

import java.util.List;
import java.util.Optional;

public interface StudentListService {

    StudentList createStudentList(StudentList studentList, Long userId);
    Optional<StudentList> getStudentListById(Long id);
    List<StudentList> getStudentListsByUserId(Long userId);
    List<StudentList> getAllStudentLists();
    StudentList updateStudentList(Long id, StudentList studentList);
    boolean deleteStudentList(Long id);
    boolean listNameExistsForUser(String name, Long userId);
}