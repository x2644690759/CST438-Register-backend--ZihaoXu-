package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.ScheduleController;
import com.cst438.controller.StudentController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
    public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";

    @MockBean
	CourseRepository courseRepository;

	@MockBean
	StudentRepository studentRepository;

	@MockBean
	EnrollmentRepository enrollmentRepository;

	@MockBean
	GradebookService gradebookService;

	@Autowired
	private MockMvc mvc;

    @Test
	public void addStudent()  throws Exception{
		MockHttpServletResponse response;

		Student student = new Student();
        student.setEmail(TEST_STUDENT_EMAIL);
        student.setName(TEST_STUDENT_NAME);

		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .content(asJsonString(student))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(200, response.getStatus());

		verify(studentRepository).save(any(Student.class));
	}

    @Test
    public void putStudent_hold() throws Exception{
        MockHttpServletResponse response;

        Student student = new Student();
        student.setEmail(TEST_STUDENT_EMAIL);
        student.setName(TEST_STUDENT_NAME);
        student.setStatusCode(0);
        student.setStudent_id(1);

        given(studentRepository.findById(1)).willReturn(Optional.of(student));

        response = mvc.perform(
				MockMvcRequestBuilders
			      .put("/student/put_hold")
			      .content(asJsonString(student))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

        assertEquals(200, response.getStatus());

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void releaseStudent_hold() throws Exception{
        MockHttpServletResponse response;

        Student student = new Student();
        student.setEmail(TEST_STUDENT_EMAIL);
        student.setName(TEST_STUDENT_NAME);
        student.setStatusCode(1);
        student.setStudent_id(1);

        given(studentRepository.findById(1)).willReturn(Optional.of(student));

        response = mvc.perform(
				MockMvcRequestBuilders
			      .put("/student/release_hold")
			      .content(asJsonString(student))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

        assertEquals(200, response.getStatus());

        verify(studentRepository).save(any(Student.class));
    }

    private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
