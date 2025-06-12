package com.groupformer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "group_draws")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_list_id", nullable = false)
    private StudentList studentList;

    @OneToMany(mappedBy = "groupDraw", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Group> groups;
}