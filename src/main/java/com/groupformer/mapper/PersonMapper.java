package com.groupformer.mapper;

import com.groupformer.dto.PersonDto;
import com.groupformer.model.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PersonMapper {

    public static Person toEntity(PersonDto dto) {
        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setGender(Person.Gender.valueOf(dto.getGender().toUpperCase()));
        person.setAge(dto.getAge());
        person.setFrenchLevel(dto.getFrenchLevel());
        person.setTechnicalLevel(dto.getTechnicalLevel());
        person.setFormerDwwm(dto.getFormerDwwm());
        person.setPersonalityProfile(Person.PersonalityProfile.valueOf(dto.getPersonalityProfile().toUpperCase()));
        return person;
    }

    public static PersonDto toDto(Person entity) {
        return new PersonDto(entity);
    }

    public static List<PersonDto> toDtoList(List<Person> entities) {
        return entities.stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }
}