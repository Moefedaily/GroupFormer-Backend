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

        if (dto.getGender() != null) {
            try {
                person.setGender(Person.Gender.valueOf(dto.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                person.setGender(Person.Gender.NOT_SPECIFIED);
            }
        } else {
            person.setGender(Person.Gender.NOT_SPECIFIED);
        }

        person.setAge(dto.getAge());
        person.setFrenchLevel(dto.getFrenchLevel());
        person.setTechnicalLevel(dto.getTechnicalLevel());
        person.setFormerDwwm(dto.getFormerDwwm());

        if (dto.getPersonalityProfile() != null) {
            try {
                person.setPersonalityProfile(Person.PersonalityProfile.valueOf(dto.getPersonalityProfile().toUpperCase()));
            } catch (IllegalArgumentException e) {
                person.setPersonalityProfile(Person.PersonalityProfile.RESERVED);
            }
        } else {
            person.setPersonalityProfile(Person.PersonalityProfile.RESERVED);
        }

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