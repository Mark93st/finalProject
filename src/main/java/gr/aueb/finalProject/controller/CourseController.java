package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        addCurrentUserToModel(model);
        return "courses";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        addCurrentUserToModel(model);
        return "add-course";
    }

    @PostMapping("/new")
    public String createCourse(@ModelAttribute Course course, Model model) {
        try {
            courseService.save(course);
            return "redirect:/courses?success=Course created successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating course: " + e.getMessage());
            addCurrentUserToModel(model);
            return "add-course";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Course course = courseService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
            model.addAttribute("course", course);
            addCurrentUserToModel(model);
            return "edit-course";
        } catch (RuntimeException e) {
            return "redirect:/courses?error=Course not found";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable("id") Long id, @ModelAttribute Course course, Model model) {
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
            addCurrentUserToModel(model);
            return "edit-course";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        try {
            courseService.deleteById(id);
            return "redirect:/courses?success=Course deleted successfully";
        } catch (RuntimeException e) {
            return "redirect:/courses?error=Error deleting course";
        }
    }

    private void addCurrentUserToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("currentUser", username);
    }
}
