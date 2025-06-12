package com.groupformer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "List name is required")
    @Size(min = 3, max = 100, message = "List name must be between 3 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "studentList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Person> persons;

    @OneToMany(mappedBy = "studentList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroupDraw> groupDraws;
}