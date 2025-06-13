package com.groupformer.serviceImpl;

import com.groupformer.model.Group;
import com.groupformer.model.GroupDraw;
import com.groupformer.model.Person;
import com.groupformer.repository.GroupDrawRepository;
import com.groupformer.repository.GroupRepository;
import com.groupformer.repository.PersonRepository;
import com.groupformer.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupDrawRepository groupDrawRepository;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Group createGroup(Group group, Long groupDrawId) {
        Optional<GroupDraw> groupDraw = groupDrawRepository.findById(groupDrawId);
        if (groupDraw.isPresent()) {
            group.setGroupDraw(groupDraw.get());
            return groupRepository.save(group);
        }
        throw new RuntimeException("GroupDraw not found with id: " + groupDrawId);
    }

    @Override
    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<Group> getGroupsByGroupDrawId(Long groupDrawId) {
        return groupRepository.findByGroupDrawId(groupDrawId);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group updateGroup(Long id, Group group) {
        Optional<Group> existingGroup = groupRepository.findById(id);
        if (existingGroup.isPresent()) {
            Group groupToUpdate = existingGroup.get();
            groupToUpdate.setName(group.getName());
            return groupRepository.save(groupToUpdate);
        }
        throw new RuntimeException("Group not found with id: " + id);
    }

    @Override
    public boolean deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Group addPersonToGroup(Long groupId, Long personId) {
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Person> person = personRepository.findById(personId);

        if (group.isPresent() && person.isPresent()) {
            Group groupToUpdate = group.get();
            if (!groupToUpdate.getPersons().contains(person.get())) {
                groupToUpdate.getPersons().add(person.get());
                return groupRepository.save(groupToUpdate);
            }
            return groupToUpdate;
        }
        throw new RuntimeException("Group or Person not found");
    }

    @Override
    public Group removePersonFromGroup(Long groupId, Long personId) {
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Person> person = personRepository.findById(personId);

        if (group.isPresent() && person.isPresent()) {
            Group groupToUpdate = group.get();
            groupToUpdate.getPersons().remove(person.get());
            return groupRepository.save(groupToUpdate);
        }
        throw new RuntimeException("Group or Person not found");
    }
}