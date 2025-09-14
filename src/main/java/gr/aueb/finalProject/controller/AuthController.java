package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.dto.RegistrationDTO;
import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationData", new RegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute RegistrationDTO registrationData, Model model) {
        try {
            User newUser = new User();
            newUser.setUsername(registrationData.getUsername());
            newUser.setPassword(registrationData.getPassword());

            long userCount = userService.findAll().size();
            if (userCount == 0) {
                newUser.setRole("ROLE_ADMIN"); // Ο πρώτος χρήστης γίνεται ADMIN
            } else {
                newUser.setRole("ROLE_STUDENT"); // Όλοι οι επόμενοι γίνονται STUDENT
            }

            Student newStudent = new Student();
            newStudent.setFirstName(registrationData.getFirstName());
            newStudent.setLastName(registrationData.getLastName());
            newStudent.setEmail(registrationData.getEmail());

            newUser.setStudent(newStudent);
            newStudent.setUser(newUser);

            userService.registerNewUser(newUser);

            model.addAttribute("successMessage", "Registration successful! Please login.");
            return "login";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("registrationData", registrationData);
            return "register";
        }
    }
}
