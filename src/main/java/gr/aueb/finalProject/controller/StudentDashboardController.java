package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.Enrollment;
import gr.aueb.finalProject.model.Course;
import gr.aueb.finalProject.service.StudentService;
import gr.aueb.finalProject.service.CourseService;
import gr.aueb.finalProject.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class StudentDashboardController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;


    @Autowired
    public StudentDashboardController(StudentService studentService,
                                      CourseService courseService,
                                      EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Βρες τον student βασισμένο στο username
        Optional<Student> student = studentService.findByUserUsername(username);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("currentUser", username);
            return "student-courses";
        } else {
            return "redirect:/home?error=Student not found";
        }
    }

    @GetMapping("/my-grades")
    public String myGrades(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Βρες τον student βασισμένο στο username
        Optional<Student> student = studentService.findByUserUsername(username);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("currentUser", username);
            return "student-grades";
        } else {
            return "redirect:/home?error=Student not found";
        }
    }

    @GetMapping("/enroll")
    public String showEnrollmentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Student> student = studentService.findByUserUsername(username);
        List<Course> availableCourses = courseService.findAll();

        if (student.isPresent()) {
            // Φίλτραρε τα μαθήματα που ο φοιτητής ΔΕΝ είναι ήδη εγγεγραμμένος
            List<Course> notEnrolledCourses = availableCourses.stream()
                    .filter(course -> !enrollmentService.existsByStudentAndCourse(student.get().getId(), course.getId()))
                    .toList();

            model.addAttribute("student", student.get());
            model.addAttribute("courses", notEnrolledCourses);
            model.addAttribute("currentUser", username);
            return "enroll-course";
        } else {
            return "redirect:/home?error=Student not found";
        }
    }

    @PostMapping("/enroll")
    public String enrollInCourse(@RequestParam Long courseId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Student> student = studentService.findByUserUsername(username);
        Optional<Course> course = courseService.findById(courseId);

        if (student.isPresent() && course.isPresent()) {
            // Έλεγχος αν είναι ήδη εγγεγραμμένος
            if (enrollmentService.existsByStudentAndCourse(student.get().getId(), courseId)) {
                model.addAttribute("errorMessage", "You are already enrolled in this course");
                return showEnrollmentPage(model);
            }

            // Δημιουργία νέας εγγραφής
            Enrollment enrollment = new Enrollment(student.get(), course.get());
            enrollmentService.save(enrollment);

            return "redirect:/my-courses?success=Enrolled successfully in " + course.get().getTitle();
        } else {
            return "redirect:/enroll?error=Enrollment failed";
        }
    }

    @PostMapping("/drop-course")
    public String dropCourse(@RequestParam Long enrollmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Student> student = studentService.findByUserUsername(username);
        Optional<Enrollment> enrollment = enrollmentService.findById(enrollmentId);

        if (student.isPresent() && enrollment.isPresent() &&
                enrollment.get().getStudent().getId().equals(student.get().getId())) {

            enrollmentService.deleteById(enrollmentId);
            return "redirect:/my-courses?success=Course dropped successfully";
        } else {
            return "redirect:/my-courses?error=Failed to drop course";
        }
    }
}
