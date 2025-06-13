package com.groupformer.repository;

import com.groupformer.model.GroupDraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDrawRepository extends JpaRepository<GroupDraw, Long> {
    List<GroupDraw> findByStudentListId(Long studentListId);
}