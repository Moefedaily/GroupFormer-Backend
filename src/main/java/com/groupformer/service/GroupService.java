package com.groupformer.service;

import com.groupformer.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    Group createGroup(Group group, Long groupDrawId);
    Optional<Group> getGroupById(Long id);
    List<Group> getGroupsByGroupDrawId(Long groupDrawId);
    List<Group> getAllGroups();
    Group updateGroup(Long id, Group group);
    boolean deleteGroup(Long id);
    Group addPersonToGroup(Long groupId, Long personId);
    Group removePersonFromGroup(Long groupId, Long personId);
}