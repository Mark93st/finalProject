package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        // Mock Security Context to prevent NullPointerException in addCurrentUserToModel
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testListCourses() {
        when(courseService.findAll()).thenReturn(Arrays.asList(new Course(), new Course()));

        String viewName = courseController.listCourses(model);

        assertEquals("courses", viewName);
        verify(model).addAttribute(eq("courses"), any());
        verify(model).addAttribute("currentUser", "testUser");
    }

    @Test
    void testCreateCourseSuccess() {
        Course course = new Course();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = courseController.createCourse(course, bindingResult, model);

        assertEquals("redirect:/courses?success=Course created successfully", viewName);
        verify(courseService).save(course);
    }

    @Test
    void testCreateCourseValidationFailure() {
        Course course = new Course();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = courseController.createCourse(course, bindingResult, model);

        assertEquals("add-course", viewName);
        verify(courseService, never()).save(any());
        verify(model).addAttribute("currentUser", "testUser");
    }

    @Test
    void testUpdateCourseSuccess() {
        Long courseId = 1L;
        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        Course updatedData = new Course();
        updatedData.setTitle("New Title");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(courseService.findById(courseId)).thenReturn(Optional.of(existingCourse));

        String viewName = courseController.updateCourse(courseId, updatedData, bindingResult, model);

        assertEquals("redirect:/courses?success=Course updated successfully", viewName);
        verify(courseService).save(existingCourse);
        assertEquals("New Title", existingCourse.getTitle());
    }

    @Test
    void testDeleteCourse() {
        String viewName = courseController.deleteCourse(1L);
        assertEquals("redirect:/courses?success=Course deleted successfully", viewName);
        verify(courseService).deleteById(1L);
    }
}