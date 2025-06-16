package com.groupformer.dto;

import com.groupformer.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private Integer frenchLevel;
    private Integer technicalLevel;
    private Boolean formerDwwm;
    private String personalityProfile;
    private Long studentListId;
    private String studentListName;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.gender = person.getGender() != null ? person.getGender().getValue() : "ne se prononce pas";
        this.age = person.getAge();
        this.frenchLevel = person.getFrenchLevel();
        this.technicalLevel = person.getTechnicalLevel();
        this.formerDwwm = person.getFormerDwwm();
        this.personalityProfile = person.getPersonalityProfile() != null ?
                person.getPersonalityProfile().getValue() : "réservé";
        if (person.getStudentList() != null) {
            this.studentListId = person.getStudentList().getId();
            this.studentListName = person.getStudentList().getName();
        }
    }
}