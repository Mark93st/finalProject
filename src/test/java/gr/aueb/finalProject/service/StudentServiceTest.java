package gr.aueb.finalProject.service;

import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

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
    void testFindAll() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList(testStudent));

        // Act
        List<Student> students = studentService.findAll();

        // Assert
        assertEquals(1, students.size());
        assertEquals("John", students.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> foundStudent = studentService.findById(1L);

        // Assert
        assertTrue(foundStudent.isPresent());
        assertEquals("John", foundStudent.get().getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> foundStudent = studentService.findById(999L);

        // Assert
        assertFalse(foundStudent.isPresent());
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    void testFindByLastName() {
        // Arrange
        when(studentRepository.findByLastNameContainingIgnoreCase("Doe")).thenReturn(Arrays.asList(testStudent));

        // Act
        List<Student> students = studentService.findByLastName("Doe");

        // Assert
        assertEquals(1, students.size());
        assertEquals("Doe", students.get(0).getLastName());
        verify(studentRepository, times(1)).findByLastNameContainingIgnoreCase("Doe");
    }

    @Test
    void testFindByUserUsername() {
        // Arrange
        when(studentRepository.findByUserUsername("johndoe")).thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> foundStudent = studentService.findByUserUsername("johndoe");

        // Assert
        assertTrue(foundStudent.isPresent());
        assertEquals("John", foundStudent.get().getFirstName());
        verify(studentRepository, times(1)).findByUserUsername("johndoe");
    }

    @Test
    void testSave() {
        // Arrange
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student savedStudent = studentService.save(testStudent);

        // Assert
        assertNotNull(savedStudent);
        assertEquals("John", savedStudent.getFirstName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testDeleteById() {
        // Act
        studentService.deleteById(1L);

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }
}