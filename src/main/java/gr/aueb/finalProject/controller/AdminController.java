package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.service.CourseService;
import gr.aueb.finalProject.service.EnrollmentService;
import gr.aueb.finalProject.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public AdminController(StudentService studentService, 
                           CourseService courseService, 
                           EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Fetch counts for the dashboard statistics
        long studentCount = studentService.count();
        long courseCount = courseService.count();
        long enrollmentCount = enrollmentService.count();

        model.addAttribute("currentUser", username);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("enrollmentCount", enrollmentCount);

        return "admin-panel";
    }
}
