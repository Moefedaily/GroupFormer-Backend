package com.groupformer.serviceImpl;

import com.groupformer.model.Person;
import com.groupformer.model.StudentList;
import com.groupformer.repository.PersonRepository;
import com.groupformer.repository.StudentListRepository;
import com.groupformer.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StudentListRepository studentListRepository;

    @Override
    public Person createPerson(Person person, Long studentListId) {
        Optional<StudentList> studentList = studentListRepository.findById(studentListId);
        if (studentList.isPresent()) {
            person.setStudentList(studentList.get());
            return personRepository.save(person);
        }
        throw new RuntimeException("StudentList not found with id: " + studentListId);
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> getPersonsByStudentListId(Long studentListId) {
        return personRepository.findByStudentListId(studentListId);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person updatePerson(Long id, Person person) {
        Optional<Person> existingPerson = personRepository.findById(id);
        if (existingPerson.isPresent()) {
            Person personToUpdate = existingPerson.get();
            personToUpdate.setName(person.getName());
            personToUpdate.setGender(person.getGender());
            personToUpdate.setAge(person.getAge());
            personToUpdate.setFrenchLevel(person.getFrenchLevel());
            personToUpdate.setTechnicalLevel(person.getTechnicalLevel());
            personToUpdate.setFormerDwwm(person.getFormerDwwm());
            personToUpdate.setPersonalityProfile(person.getPersonalityProfile());
            return personRepository.save(personToUpdate);
        }
        throw new RuntimeException("Person not found with id: " + id);
    }

    @Override
    public boolean deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean personNameExistsInList(String name, Long studentListId) {
        Optional<StudentList> studentList = studentListRepository.findById(studentListId);
        if (studentList.isPresent()) {
            return personRepository.existsByNameAndStudentList(name, studentList.get());
        }
        return false;
    }
}