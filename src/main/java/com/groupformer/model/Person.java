package com.groupformer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 99, message = "Age must be less than 100")
    @Column(name = "age", nullable = false)
    private Integer age;

    @Min(value = 1, message = "French level must be between 1 and 4")
    @Max(value = 4, message = "French level must be between 1 and 4")
    @Column(name = "french_level", nullable = false)
    private Integer frenchLevel;

    @Min(value = 1, message = "Technical level must be between 1 and 4")
    @Max(value = 4, message = "Technical level must be between 1 and 4")
    @Column(name = "technical_level", nullable = false)
    private Integer technicalLevel;

    @Column(name = "former_dwwm", nullable = false)
    private Boolean formerDwwm = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "personality_profile", nullable = false, length = 20)
    private PersonalityProfile personalityProfile;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_list_id", nullable = false)
    private StudentList studentList;

    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY)
    private List<Group> groups;

    public enum Gender {
        MALE("masculin"),
        FEMALE("féminin"),
        NOT_SPECIFIED("ne se prononce pas");

        private final String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PersonalityProfile {
        SHY("timide"),
        RESERVED("réservé"),
        COMFORTABLE("à l'aise");

        private final String value;

        PersonalityProfile(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}