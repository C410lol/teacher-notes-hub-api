package com.api.notebook.services;

import com.api.notebook.models.AuthModel;
import com.api.notebook.models.AuthReturnModel;
import com.api.notebook.models.entities.NotebookEntity;
import com.api.notebook.models.entities.TeacherEntity;
import com.api.notebook.models.entities.VerificationCodeEntity;
import com.api.notebook.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherEntity createTeacher(@NotNull TeacherEntity teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        return teacherRepository.save(teacher);
    }

    public void editTeacher(TeacherEntity teacher) {
        teacherRepository.save(teacher);
    }

    public List<TeacherEntity> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<TeacherEntity> findTeacherById(UUID id) {
        return teacherRepository.findById(id);
    }

    public Optional<TeacherEntity> findTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public void deleteTeacherById(UUID id) {
        teacherRepository.deleteById(id);
    }

    //Try to authenticate teacher
    public AuthReturnModel tryToAuthenticateTeacher(@NotNull AuthModel authModel, JwtService jwtService) {
        var teacherOptional = findTeacherByEmail(authModel.getEmail());
        if (teacherOptional.isPresent()) { //Verify if the teacher exists

            //Verify if the auth model password matches the found teacher entity
            if (passwordEncoder.matches(authModel.getPassword(), teacherOptional.get().getPassword())) {
                var token = jwtService.generateToken(teacherOptional.get().getEmail());
                return new AuthReturnModel(
                        teacherOptional.get().getId(),
                        token);
            }
        }
        return null;
    }

    public void setVerificationCodeToTeacher(UUID teacherId, @NotNull VerificationCodeEntity verificationCode) {
        var teacherOptional = findTeacherById(teacherId);
        teacherOptional.ifPresent(verificationCode::setTeacher);
    }

    public void setNotebookToTeacher(UUID teacherId, @NotNull NotebookEntity notebook) { //Set notebook to a teacher
        var teacherOptional = findTeacherById(teacherId);
        teacherOptional.ifPresent(notebook::setTeacher);
    }

}
