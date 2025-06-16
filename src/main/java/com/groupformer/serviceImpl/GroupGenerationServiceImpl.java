package com.groupformer.serviceImpl;

import com.groupformer.dto.GenerateGroupsRequest;
import com.groupformer.model.*;
import com.groupformer.repository.*;
import com.groupformer.service.GroupGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupGenerationServiceImpl implements GroupGenerationService {

    @Autowired
    private StudentListRepository studentListRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GroupDrawRepository groupDrawRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public GroupDraw generateGroups(GenerateGroupsRequest request) {
        System.out.println("Wee are in the group generation");
        System.out.println("the request: " + request);

        StudentList studentList = studentListRepository.findById(request.getStudentListId())
                .orElseThrow(() -> new RuntimeException("Student list not found"));

        List<Person> persons = personRepository.findByStudentListId(request.getStudentListId());
        System.out.println("Found " + persons.size() + " persons in student list: " + studentList.getName());

        if (persons.size() < request.getNumberOfGroups()) {
            throw new RuntimeException("Not enough persons to create " + request.getNumberOfGroups() + " groups");
        }

        GroupDraw groupDraw = new GroupDraw();
        groupDraw.setStudentList(studentList);
        groupDraw = groupDrawRepository.save(groupDraw);
        System.out.println("Created GroupDraw with ID: " + groupDraw.getId());

        List<Group> groups = createEmptyGroups(request, groupDraw);

        initialDistribution(persons, groups);
        applySelectedCriteria(groups, request);
        groupRepository.saveAll(groups);
        groupDraw.setGroups(groups);

        System.out.println("Group generation completed");
        return groupDraw;
    }


    private List<Group> createEmptyGroups(GenerateGroupsRequest request, GroupDraw groupDraw) {
        System.out.println("Creating " + request.getNumberOfGroups() + " empty groups");

        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < request.getNumberOfGroups(); i++) {
            Group group = new Group();
            group.setName(request.getGroupNames().get(i));
            group.setGroupDraw(groupDraw);
            group.setPersons(new ArrayList<>());
            groups.add(group);
        }

        return groups;
    }

    private void initialDistribution(List<Person> persons, List<Group> groups) {
        System.out.println("Starting a random distribution");

        Collections.shuffle(persons);
        System.out.println("Shuffled all persons randomly");

        for (int i = 0; i < persons.size(); i++) {
            Group targetGroup = groups.get(i % groups.size());
            targetGroup.getPersons().add(persons.get(i));
            System.out.println(persons.get(i).getName() + " assigned to " + targetGroup.getName());
        }
    }

    private void applySelectedCriteria(List<Group> groups, GenerateGroupsRequest request) {
        System.out.println("Applying selected criteria");

        if (request.getMixGender()) {
            System.out.println("Applying gender rebalancing");
            rebalanceByGender(groups);
        }

        if (request.getMixTechnicalLevel()) {
            System.out.println("Applying technical level rebalancing");
            rebalanceByTechnicalLevel(groups);
        }

        if (request.getMixAge()) {
            System.out.println("Applying age rebalancing");
            rebalanceByAge(groups);
        }

        if (request.getMixFormerDwwm()) {
            System.out.println("Applying former DWWM rebalancing");
            rebalanceByFormerDwwm(groups);
        }

        if (request.getMixFrenchLevel()) {
            System.out.println("Applying French level rebalancing");
            rebalanceByFrenchLevel(groups);
        }

        if (request.getMixPersonalityProfile()) {
            System.out.println("Applying personality profile rebalancing");
            rebalanceByPersonalityProfile(groups);
        }
    }

    private void rebalanceByGender(List<Group> groups) {
        List<Person> allPersons = extractAllPersons(groups);
        clearAllGroups(groups);

        Map<Person.Gender, List<Person>> genderGroups = allPersons.stream()
                .collect(Collectors.groupingBy(Person::getGender));

        System.out.println("Grouped by gender:");
        for (Map.Entry<Person.Gender, List<Person>> entry : genderGroups.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " persons");
        }

        for (Map.Entry<Person.Gender, List<Person>> entry : genderGroups.entrySet()) {
            List<Person> genderPersons = entry.getValue();
            Collections.shuffle(genderPersons);
            distributeRoundRobin(genderPersons, groups);
        }
    }

    private void rebalanceByTechnicalLevel(List<Group> groups) {
        List<Person> allPersons = extractAllPersons(groups);
        clearAllGroups(groups);

        Map<Integer, List<Person>> techGroups = allPersons.stream()
                .collect(Collectors.groupingBy(Person::getTechnicalLevel));

        System.out.println("Grouped by technical level:");
        for (Map.Entry<Integer, List<Person>> entry : techGroups.entrySet()) {
            System.out.println("Level " + entry.getKey() + ": " + entry.getValue().size() + " persons");
        }

        for (Map.Entry<Integer, List<Person>> entry : techGroups.entrySet()) {
            List<Person> techPersons = entry.getValue();
            Collections.shuffle(techPersons);
            distributeRoundRobin(techPersons, groups);
        }
    }

    private void rebalanceByAge(List<Group> groups) {
        List<Person> allPersons = extractAllPersons(groups);
        clearAllGroups(groups);

        allPersons.sort(Comparator.comparing(Person::getAge));
        System.out.println("Sorted by age for balanced distribution");

        distributeRoundRobin(allPersons, groups);
    }

    private void rebalanceByFormerDwwm(List<Group> groups) {
        List<Person> allPersons = extractAllPersons(groups);
        clearAllGroups(groups);

        Map<Boolean, List<Person>> dwwmGroups = allPersons.stream()
                .collect(Collectors.groupingBy(Person::getFormerDwwm));

        System.out.println("Grouped by former DWWM:");
        for (Map.Entry<Boolean, List<Person>> entry : dwwmGroups.entrySet()) {
            String label = entry.getKey() ? "Former DWWM" : "Not former DWWM";
            System.out.println(label + ": " + entry.getValue().size() + " persons");
        }

        for (Map.Entry<Boolean, List<Person>> entry : dwwmGroups.entrySet()) {
            List<Person> dwwmPersons = entry.getValue();
            Collections.shuffle(dwwmPersons);
            distributeRoundRobin(dwwmPersons, groups);
        }
    }
    private void rebalanceByFrenchLevel(List<Group> groups) {
        List<Person> allPersons = extractAllPersons(groups);
        clearAllGroups(groups);

        Map<Integer, List<Person>> frenchGroups = allPersons.stream()
                .collect(Collectors.groupingBy(Person::getFrenchLevel));

        System.out.println("Grouped by French level:");
        for (Map.Entry<Integer, List<Person>> entry : frenchGroups.entrySet()) {
            System.out.println("Level " + entry.getKey() + ": " + entry.getValue().size() + " persons");
        }

        for (Map.Entry<Integer, List<Person>> entry : frenchGroups.entrySet()) {
            List<Person> frenchPersons = entry.getValue();
            Collections.shuffle(frenchPersons);
            distributeRoundRobin(frenchPersons, groups);
        }
    }

    private void rebalanceByPersonalityProfile(List<Group> groups) {
        List<Person> allPersons = extractAllPersons(groups);
        clearAllGroups(groups);

        Map<Person.PersonalityProfile, List<Person>> profileGroups = allPersons.stream()
                .collect(Collectors.groupingBy(Person::getPersonalityProfile));

        System.out.println("Grouped by personality profile:");
        for (Map.Entry<Person.PersonalityProfile, List<Person>> entry : profileGroups.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " persons");
        }

        for (Map.Entry<Person.PersonalityProfile, List<Person>> entry : profileGroups.entrySet()) {
            List<Person> profilePersons = entry.getValue();
            Collections.shuffle(profilePersons);
            distributeRoundRobin(profilePersons, groups);
        }
    }
    private List<Person> extractAllPersons(List<Group> groups) {
        return groups.stream()
                .flatMap(group -> group.getPersons().stream())
                .collect(Collectors.toList());
    }

    private void clearAllGroups(List<Group> groups) {
        groups.forEach(group -> group.getPersons().clear());
    }

    private void distributeRoundRobin(List<Person> persons, List<Group> groups) {
        for (int i = 0; i < persons.size(); i++) {
            Group targetGroup = groups.get(i % groups.size());
            targetGroup.getPersons().add(persons.get(i));
        }
    }

}