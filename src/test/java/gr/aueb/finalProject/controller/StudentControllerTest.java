package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.service.StudentService;
import gr.aueb.finalProject.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private StudentController studentController;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john.doe@example.com");
    }

    @Test
    void testShowEditFormNotFound() {
        // Arrange
        when(studentService.findById(999L)).thenReturn(Optional.empty());

        // Act
        String viewName = studentController.showEditForm(999L, model);

        // Assert
        assertEquals("redirect:/students?error=Student not found", viewName);
    }

    @Test
    void testDeleteStudentSuccess() {
        // Act
        String viewName = studentController.deleteStudent(1L);

        // Assert
        assertEquals("redirect:/students?success=Student deleted successfully", viewName);
        verify(studentService, times(1)).deleteById(1L);
    }

    @Test
    void testCreateStudentSuccess() {
        // Arrange
        when(userService.registerNewUser(any(User.class))).thenReturn(new User());

        // Act
        String viewName = studentController.createStudent(testStudent, model);

        // Assert
        assertEquals("redirect:/students?success=true", viewName);
        verify(userService, times(1)).registerNewUser(any(User.class));
    }

    @Test
    void testUpdateStudentSuccess() {
        // Arrange
        when(studentService.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentService.save(any(Student.class))).thenReturn(testStudent);

        // Act
        String viewName = studentController.updateStudent(1L, testStudent, model);

        // Assert
        assertEquals("redirect:/students?success=Student updated successfully", viewName);
        verify(studentService, times(1)).findById(1L);
        verify(studentService, times(1)).save(any(Student.class));
    }
}