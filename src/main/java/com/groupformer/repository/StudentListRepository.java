package com.groupformer.repository;

import com.groupformer.model.StudentList;
import com.groupformer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentListRepository extends JpaRepository<StudentList, Long> {
    List<StudentList> findByUserId(Long userId);
    boolean existsByNameAndUser(String name, User user);
}