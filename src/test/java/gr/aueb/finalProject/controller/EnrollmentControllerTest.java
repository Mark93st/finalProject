package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.model.Enrollment;
import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.service.CourseService;
import gr.aueb.finalProject.service.EnrollmentService;
import gr.aueb.finalProject.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @Mock
    private Model model;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private Enrollment testEnrollment;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Mathematics");

        testEnrollment = new Enrollment();
        testEnrollment.setId(1L);
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
    }

    @Test
    void testCreateEnrollmentSuccess() {
        when(studentService.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseService.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentService.existsByStudentAndCourse(1L, 1L)).thenReturn(false);
        when(enrollmentService.save(any(Enrollment.class))).thenReturn(testEnrollment);

        String viewName = enrollmentController.createEnrollment(1L, 1L, model);

        assertEquals("redirect:/enrollments?success=Enrollment created successfully", viewName);
        verify(enrollmentService).save(any(Enrollment.class));
    }

    @Test
    void testDeleteEnrollmentSuccess() {
        String viewName = enrollmentController.deleteEnrollment(1L);

        assertEquals("redirect:/enrollments?success=Enrollment deleted successfully", viewName);
        verify(enrollmentService).deleteById(1L);
    }

    @Test
    void testUpdateGradeSuccess() {
        when(enrollmentService.findById(1L)).thenReturn(Optional.of(testEnrollment));
        when(enrollmentService.save(any(Enrollment.class))).thenReturn(testEnrollment);

        String viewName = enrollmentController.updateGrade(1L, "A", model);

        assertEquals("redirect:/enrollments?success=Grade updated successfully", viewName);
        verify(enrollmentService).save(any(Enrollment.class));
    }
}