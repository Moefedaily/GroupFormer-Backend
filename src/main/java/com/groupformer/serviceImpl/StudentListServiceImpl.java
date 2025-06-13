package com.groupformer.serviceimpl;

import com.groupformer.model.StudentList;
import com.groupformer.model.User;
import com.groupformer.repository.StudentListRepository;
import com.groupformer.repository.UserRepository;
import com.groupformer.service.StudentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentListServiceImpl implements StudentListService {

    @Autowired
    private StudentListRepository studentListRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public StudentList createStudentList(StudentList studentList, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            studentList.setUser(user.get());
            return studentListRepository.save(studentList);
        }
        throw new RuntimeException("User not found with id: " + userId);
    }

    @Override
    public Optional<StudentList> getStudentListById(Long id) {
        return studentListRepository.findById(id);
    }

    @Override
    public List<StudentList> getStudentListsByUserId(Long userId) {
        return studentListRepository.findByUserId(userId);
    }

    @Override
    public List<StudentList> getAllStudentLists() {
        return studentListRepository.findAll();
    }

    @Override
    public StudentList updateStudentList(Long id, StudentList studentList) {
        Optional<StudentList> existingList = studentListRepository.findById(id);
        if (existingList.isPresent()) {
            StudentList listToUpdate = existingList.get();
            listToUpdate.setName(studentList.getName());
            return studentListRepository.save(listToUpdate);
        }
        throw new RuntimeException("StudentList not found with id: " + id);
    }

    @Override
    public boolean deleteStudentList(Long id) {
        if (studentListRepository.existsById(id)) {
            studentListRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean listNameExistsForUser(String name, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return studentListRepository.existsByNameAndUser(name, user.get());
        }
        return false;
    }
}