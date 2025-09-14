package gr.aueb.finalProject.service;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.repository.CourseRepository;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Mathematics");
        testCourse.setDescription("Basic math course");
        testCourse.setCredits(5);
    }

    @Test
    void testFindAll() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));

        // Act
        List<Course> courses = courseService.findAll();

        // Assert
        assertEquals(1, courses.size());
        assertEquals("Mathematics", courses.get(0).getTitle());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Optional<Course> foundCourse = courseService.findById(1L);

        // Assert
        assertTrue(foundCourse.isPresent());
        assertEquals("Mathematics", foundCourse.get().getTitle());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Course> foundCourse = courseService.findById(999L);

        // Assert
        assertFalse(foundCourse.isPresent());
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void testSave() {
        // Arrange
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course savedCourse = courseService.save(testCourse);

        // Assert
        assertNotNull(savedCourse);
        assertEquals("Mathematics", savedCourse.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testDeleteById() {
        // Act
        courseService.deleteById(1L);

        // Assert
        verify(courseRepository, times(1)).deleteById(1L);
    }
}