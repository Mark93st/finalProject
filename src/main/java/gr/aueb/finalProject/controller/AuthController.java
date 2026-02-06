package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.dto.RegistrationDTO;
import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

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
    public String registerNewUser(@Valid @ModelAttribute("registrationData") RegistrationDTO registrationData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            User registeredUser = userService.registerNewUser(registrationData);

            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        registeredUser.getUsername(),
                        registrationData.getPassword()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return "redirect:/home?success=Registration successful! Welcome!";
            } catch (Exception e) {
                return "redirect:/login?success=Registration successful! Please login.";
            }

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("registrationData", registrationData);
            return "register";
        }
    }
}