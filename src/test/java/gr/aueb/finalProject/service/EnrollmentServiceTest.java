package gr.aueb.finalProject.service;

import gr.aueb.finalProject.model.Enrollment;
import gr.aueb.finalProject.repository.EnrollmentRepository;
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
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Enrollment testEnrollment;

    @BeforeEach
    void setUp() {
        testEnrollment = new Enrollment();
        testEnrollment.setId(1L);
    }

    @Test
    void testFindAll() {
        // Arrange
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(testEnrollment));

        // Act
        List<Enrollment> enrollments = enrollmentService.findAll();

        // Assert
        assertEquals(1, enrollments.size());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(testEnrollment));

        // Act
        Optional<Enrollment> foundEnrollment = enrollmentService.findById(1L);

        // Assert
        assertTrue(foundEnrollment.isPresent());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Enrollment> foundEnrollment = enrollmentService.findById(999L);

        // Assert
        assertFalse(foundEnrollment.isPresent());
        verify(enrollmentRepository, times(1)).findById(999L);
    }

    @Test
    void testFindByStudentId() {
        // Arrange
        when(enrollmentRepository.findByStudentId(1L)).thenReturn(Arrays.asList(testEnrollment));

        // Act
        List<Enrollment> enrollments = enrollmentService.findByStudentId(1L);

        // Assert
        assertEquals(1, enrollments.size());
        verify(enrollmentRepository, times(1)).findByStudentId(1L);
    }

    @Test
    void testFindByCourseId() {
        // Arrange
        when(enrollmentRepository.findByCourseId(1L)).thenReturn(Arrays.asList(testEnrollment));

        // Act
        List<Enrollment> enrollments = enrollmentService.findByCourseId(1L);

        // Assert
        assertEquals(1, enrollments.size());
        verify(enrollmentRepository, times(1)).findByCourseId(1L);
    }

    @Test
    void testExistsByStudentAndCourse() {
        // Arrange
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        // Act
        boolean exists = enrollmentService.existsByStudentAndCourse(1L, 1L);

        // Assert
        assertTrue(exists);
        verify(enrollmentRepository, times(1)).existsByStudentIdAndCourseId(1L, 1L);
    }

    @Test
    void testSave() {
        // Arrange
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(testEnrollment);

        // Act
        Enrollment savedEnrollment = enrollmentService.save(testEnrollment);

        // Assert
        assertNotNull(savedEnrollment);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testDeleteById() {
        // Act
        enrollmentService.deleteById(1L);

        // Assert
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}