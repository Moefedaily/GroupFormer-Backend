package com.groupformer.service;

import com.groupformer.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person createPerson(Person person, Long studentListId);
    Optional<Person> getPersonById(Long id);
    List<Person> getPersonsByStudentListId(Long studentListId);
    List<Person> getAllPersons();
    Person updatePerson(Long id, Person person);
    boolean deletePerson(Long id);
    boolean personNameExistsInList(String name, Long studentListId);
}