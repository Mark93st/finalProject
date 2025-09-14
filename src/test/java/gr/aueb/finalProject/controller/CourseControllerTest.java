package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.service.CourseService;
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
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private Model model;

    @InjectMocks
    private CourseController courseController;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Mathematics");
        testCourse.setDescription("Math course");
        testCourse.setCredits(5);
    }

    @Test
    void testCreateCourseSuccess() {
        when(courseService.save(any(Course.class))).thenReturn(testCourse);

        String viewName = courseController.createCourse(testCourse, model);

        assertEquals("redirect:/courses?success=Course created successfully", viewName);
        verify(courseService).save(any(Course.class));
    }

    @Test
    void testShowEditFormNotFound() {
        when(courseService.findById(999L)).thenReturn(Optional.empty());

        String viewName = courseController.showEditForm(999L, model);

        assertEquals("redirect:/courses?error=Course not found", viewName);
    }

    @Test
    void testUpdateCourseSuccess() {
        when(courseService.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseService.save(any(Course.class))).thenReturn(testCourse);

        String viewName = courseController.updateCourse(1L, testCourse, model);

        assertEquals("redirect:/courses?success=Course updated successfully", viewName);
        verify(courseService).save(any(Course.class));
    }


    @Test
    void testDeleteCourseSuccess() {
        String viewName = courseController.deleteCourse(1L);

        assertEquals("redirect:/courses?success=Course deleted successfully", viewName);
        verify(courseService).deleteById(1L);
    }

    @Test
    void testDeleteCourseFailure() {
        doThrow(new RuntimeException("Error")).when(courseService).deleteById(1L);

        String viewName = courseController.deleteCourse(1L);

        assertEquals("redirect:/courses?error=Error deleting course", viewName);
    }
}