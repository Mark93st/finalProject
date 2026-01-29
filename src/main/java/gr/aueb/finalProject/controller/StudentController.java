package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.service.StudentService;
import gr.aueb.finalProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('ADMIN')")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "add-student";
        }
        // Note: This creates a Student entity. If you want them to log in, 
        // you would typically create a User entity first (like in AuthController).
        studentService.save(student);
        return "redirect:/students?success=Student created successfully";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "edit-student";
                })
                .orElse("redirect:/students?error=Student not found");
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStudent(@PathVariable("id") Long id, @Valid @ModelAttribute Student student, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-student";
        }
        try {
            Student existingStudent = studentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            
            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setEmail(student.getEmail());
            
            studentService.save(existingStudent);
            return "redirect:/students?success=Student updated successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "edit-student";
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStudent(@PathVariable("id") Long id) {
        try {
            studentService.deleteById(id);
            return "redirect:/students?success=Student deleted successfully";
        } catch (Exception e) {
            return "redirect:/students?error=Error deleting student";
        }
    }
}