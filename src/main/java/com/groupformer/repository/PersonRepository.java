package com.groupformer.repository;

import com.groupformer.model.Person;
import com.groupformer.model.StudentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByStudentListId(Long studentListId);
    boolean existsByNameAndStudentList(String name, StudentList studentList);
}