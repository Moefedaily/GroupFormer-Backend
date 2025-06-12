package com.groupformer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Group name is required")
    @Size(min = 2, max = 100, message = "Group name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_draw_id", nullable = false)
    private GroupDraw groupDraw;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_persons",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> persons;
}