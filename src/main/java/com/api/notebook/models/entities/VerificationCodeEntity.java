package com.api.notebook.models.entities;

import com.api.notebook.enums.VerificationCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "verification_codes")
public class VerificationCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VerificationCodeEnum type;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;

}
