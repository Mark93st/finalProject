package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.service.StudentService;
import gr.aueb.finalProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    @Autowired
    public StudentController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.findAll();
        model.addAttribute("students", students);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("currentUser", username);
        return "students";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("currentUser", username);
        return "add-student";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Student student = studentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
            model.addAttribute("student", student);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            model.addAttribute("currentUser", username);
            return "edit-student";
        } catch (RuntimeException e) {
            return "redirect:/students?error=Student not found";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        try {
            studentService.deleteById(id);
            return "redirect:/students?success=Student deleted successfully";
        } catch (RuntimeException e) {
            return "redirect:/students?error=Error deleting student";
        }
    }

    @PostMapping("/new")
    public String createStudent(@ModelAttribute Student student, Model model) {
        try {
            User newUser = new User();
            newUser.setUsername(student.getFirstName().toLowerCase() + "." + student.getLastName().toLowerCase());
            newUser.setPassword("tempPassword123");
            newUser.setRole("ROLE_STUDENT");

            student.setUser(newUser);
            newUser.setStudent(student);

            userService.registerNewUser(newUser);

            return "redirect:/students?success=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating student: " + e.getMessage());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            model.addAttribute("currentUser", username);
            return "add-student";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute Student student, Model model) {
        try {
            Student existingStudent = studentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setEmail(student.getEmail());

            studentService.save(existingStudent);
            return "redirect:/students?success=Student updated successfully";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Error updating student: " + e.getMessage());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            model.addAttribute("currentUser", username);
            return "edit-student";
        }
    }
}
