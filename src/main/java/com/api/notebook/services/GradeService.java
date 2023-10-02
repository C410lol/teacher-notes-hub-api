package com.api.notebook.services;

import com.api.notebook.models.entities.GradeEntity;
import com.api.notebook.repositories.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public void saveGrade(GradeEntity grade) {
        gradeRepository.save(grade);
    }

    public List<GradeEntity> findAllGrades() {
        return gradeRepository.findAll();
    }

    public List<GradeEntity> findAllGradesByWorkId(@NotNull List<GradeEntity> grades, Long workId) {
        List<GradeEntity> workGrades = new ArrayList<>();
        for (GradeEntity grade:
                grades) {
            if (grade.getWork().getId().equals(workId)) {
                workGrades.add(grade);
            }
        }
        return workGrades;
    }

    public Optional<GradeEntity> findGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public void deleteGradeById(Long id) {
        gradeRepository.deleteById(id);
    }

}
