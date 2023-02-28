package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
public class StudentController {
    @Autowired
	StudentRepository studentRepository;

    @PostMapping("/student")
    public Student addStudent(@RequestBody Student student) {
        Student s = studentRepository.findByEmail(student.getEmail());

        if(s == null){
            return studentRepository.save(student);
        }else{
            return null;
        }
    }

    @PutMapping("/student/put_hold")
    public Student putStudent_hold(@RequestBody Student student) {
        Student student1 = studentRepository.findById(student.getStudent_id()).orElse(null);

        if(student1 != null && student1.getStatusCode() != 1){
            student1.setStatusCode(1);

            return studentRepository.save(student1);
        }else{
            return null;
        }
    }

    @PutMapping("/student/release_hold")
    public Student releaseStudent_hold(@RequestBody Student student) {
        Student student1 = studentRepository.findById(student.getStudent_id()).orElse(null);

        if(student1 != null && student1.getStatusCode() != 0){
            student1.setStatusCode(0);

            return studentRepository.save(student1);
        }else{
            return null;
        }
    }
}
