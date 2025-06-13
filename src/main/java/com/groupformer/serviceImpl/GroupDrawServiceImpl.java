package com.groupformer.serviceImpl;

import com.groupformer.model.GroupDraw;
import com.groupformer.model.StudentList;
import com.groupformer.repository.GroupDrawRepository;
import com.groupformer.repository.StudentListRepository;
import com.groupformer.service.GroupDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupDrawServiceImpl implements GroupDrawService {

    @Autowired
    private GroupDrawRepository groupDrawRepository;

    @Autowired
    private StudentListRepository studentListRepository;

    @Override
    public GroupDraw createGroupDraw(GroupDraw groupDraw, Long studentListId) {
        Optional<StudentList> studentList = studentListRepository.findById(studentListId);
        if (studentList.isPresent()) {
            groupDraw.setStudentList(studentList.get());
            return groupDrawRepository.save(groupDraw);
        }
        throw new RuntimeException("StudentList not found with id: " + studentListId);
    }

    @Override
    public Optional<GroupDraw> getGroupDrawById(Long id) {
        return groupDrawRepository.findById(id);
    }

    @Override
    public List<GroupDraw> getGroupDrawsByStudentListId(Long studentListId) {
        return groupDrawRepository.findByStudentListId(studentListId);
    }

    @Override
    public List<GroupDraw> getAllGroupDraws() {
        return groupDrawRepository.findAll();
    }

    @Override
    public boolean deleteGroupDraw(Long id) {
        if (groupDrawRepository.existsById(id)) {
            groupDrawRepository.deleteById(id);
            return true;
        }
        return false;
    }
}