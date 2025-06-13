package com.groupformer.service;

import com.groupformer.model.GroupDraw;

import java.util.List;
import java.util.Optional;

public interface GroupDrawService {
    GroupDraw createGroupDraw(GroupDraw groupDraw, Long studentListId);
    Optional<GroupDraw> getGroupDrawById(Long id);
    List<GroupDraw> getGroupDrawsByStudentListId(Long studentListId);
    List<GroupDraw> getAllGroupDraws();
    boolean deleteGroupDraw(Long id);
}