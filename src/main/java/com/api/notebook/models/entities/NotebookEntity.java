package com.api.notebook.models.entities;

import com.api.notebook.enums.BimesterEnum;
import com.api.notebook.enums.ClassEnum;
import com.api.notebook.enums.StatusEnum;
import com.api.notebook.enums.SubjectEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Data
@Table(name = "notebooks")
public class NotebookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "classe")
    @Enumerated(value = EnumType.STRING)
    private ClassEnum classe;

    @Column(name = "subject")
    @Enumerated(value = EnumType.STRING)
    private SubjectEnum subject;

    @Column(name = "bimester")
    @Enumerated(value = EnumType.STRING)
    private BimesterEnum bimester;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;

    @ManyToMany
    @JoinTable(
            name = "notebooks_students",
            joinColumns = {@JoinColumn(name = "notebook_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private List<StudentEntity> students;

    @OneToMany(mappedBy = "notebook", cascade = {CascadeType.ALL})
    private List<LessonEntity> lessons;

    @OneToMany(mappedBy = "notebook", cascade = {CascadeType.ALL})
    private List<WorkEntity> works;

}