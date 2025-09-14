package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Enrollment;
import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.service.EnrollmentService;
import gr.aueb.finalProject.service.StudentService;
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
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService,
                                StudentService studentService,
                                CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listEnrollments(Model model) {
        List<Enrollment> enrollments = enrollmentService.findAll();
        model.addAttribute("enrollments", enrollments);
        addCurrentUserToModel(model);
        return "enrollments";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Student> students = studentService.findAll();
        List<Course> courses = courseService.findAll();

        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        addCurrentUserToModel(model);
        return "add-enrollment";
    }

    @PostMapping("/new")
    public String createEnrollment(@RequestParam Long studentId,
                                   @RequestParam Long courseId,
                                   Model model) {
        try {
            if (enrollmentService.existsByStudentAndCourse(studentId, courseId)) {
                model.addAttribute("errorMessage", "Student is already enrolled in this course");
                return showCreateForm(model);
            }

            Student student = studentService.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            Course course = courseService.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Enrollment enrollment = new Enrollment(student, course);
            enrollmentService.save(enrollment);

            return "redirect:/enrollments?success=Enrollment created successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating enrollment: " + e.getMessage());
            return showCreateForm(model);
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable("id") Long id) {
        try {
            enrollmentService.deleteById(id);
            return "redirect:/enrollments?success=Enrollment deleted successfully";
        } catch (RuntimeException e) {
            return "redirect:/enrollments?error=Error deleting enrollment";
        }
    }

    private void addCurrentUserToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("currentUser", username);
    }

    @PostMapping("/grade/{id}")
    public String updateGrade(@PathVariable("id") Long id,
                              @RequestParam String grade,
                              Model model) {
        try {
            Enrollment enrollment = enrollmentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));

            enrollment.setGrade(grade);
            enrollmentService.save(enrollment);

            return "redirect:/enrollments?success=Grade updated successfully";
        } catch (RuntimeException e) {
            return "redirect:/enrollments?error=Error updating grade";
        }
    }
}
