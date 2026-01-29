package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.model.Enrollment;
import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.service.CourseService;
import gr.aueb.finalProject.service.EnrollmentService;
import gr.aueb.finalProject.service.StudentService;
import gr.aueb.finalProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService, StudentService studentService, CourseService courseService, UserService userService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    public String listEnrollments(Authentication auth, Model model) {
        String username = auth.getName();
        
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("enrollments", enrollmentService.findAll());
        } else {
            User user = userService.findByUsername(username).orElseThrow();
            if (user.getStudent() != null) {
                model.addAttribute("enrollments", enrollmentService.findByStudentId(user.getStudent().getId()));
            } else {
                model.addAttribute("enrollments", Collections.emptyList());
                model.addAttribute("errorMessage", "No student profile found for your account.");
            }
        }
        return "enrollments";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "add-enrollment";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createEnrollmentForAdmin(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        try {
            if (enrollmentService.existsByStudentAndCourse(studentId, courseId)) {
                model.addAttribute("errorMessage", "Student is already enrolled in this course");
                model.addAttribute("students", studentService.findAll());
                model.addAttribute("courses", courseService.findAll());
                return "add-enrollment";
            }
            Student student = studentService.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
            Course course = courseService.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
            Enrollment enrollment = new Enrollment(student, course);
            enrollmentService.save(enrollment);
            return "redirect:/enrollments?success=Enrollment created successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("courses", courseService.findAll());
            return "add-enrollment";
        }
    }

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public String createEnrollment(@RequestParam("courseId") Long courseId, @RequestParam(value = "studentId", required = false) Long studentId, Authentication auth, Model model) {
        try {
            Long finalStudentId = studentId;
            if (finalStudentId == null) {
                User user = userService.findByUsername(auth.getName()).orElseThrow();
                if (user.getStudent() == null) {
                    return "redirect:/courses?error=No student profile found for your account.";
                }
                finalStudentId = user.getStudent().getId();
            }

            if (enrollmentService.existsByStudentAndCourse(finalStudentId, courseId)) {
                return "redirect:/courses?error=You are already enrolled in this course";
            }

            Student student = studentService.findById(finalStudentId).orElseThrow();
            Course course = courseService.findById(courseId).orElseThrow();

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollmentService.save(enrollment);

            return "redirect:/enrollments?success=Enrollment created successfully";
        } catch (Exception e) {
            return "redirect:/courses?error=" + e.getMessage();
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEnrollment(@PathVariable("id") Long id) {
        enrollmentService.deleteById(id);
        return "redirect:/enrollments?success=Enrollment deleted successfully";
    }

    @PostMapping("/grade/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateGrade(@PathVariable("id") Long id, @RequestParam("grade") String grade, Model model) {
        try {
            Enrollment enrollment = enrollmentService.findById(id).orElseThrow();
            enrollment.setGrade(grade);
            enrollmentService.save(enrollment);
            return "redirect:/enrollments?success=Grade updated successfully";
        } catch (Exception e) {
            return "redirect:/enrollments?error=Error updating grade";
        }
    }
}