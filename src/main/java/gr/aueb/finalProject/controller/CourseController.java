package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCourse(@Valid @ModelAttribute Course course, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "add-course";
        }
        try {
            courseService.save(course);
            return "redirect:/courses?success=Course created successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating course: " + e.getMessage());
            return "add-course";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Course course = courseService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
            model.addAttribute("course", course);
            return "edit-course";
        } catch (RuntimeException e) {
            return "redirect:/courses?error=Course not found";
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateCourse(@PathVariable("id") Long id, @Valid @ModelAttribute Course course, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-course";
        }
        try {
            Course existingCourse = courseService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

            existingCourse.setTitle(course.getTitle());
            existingCourse.setDescription(course.getDescription());
            existingCourse.setCredits(course.getCredits());

            courseService.save(existingCourse);
            return "redirect:/courses?success=Course updated successfully";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Error updating course: " + e.getMessage());
            return "edit-course";
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCourse(@PathVariable("id") Long id) {
        try {
            courseService.deleteById(id);
            return "redirect:/courses?success=Course deleted successfully";
        } catch (RuntimeException e) {
            return "redirect:/courses?error=Error deleting course";
        }
    }
}
