package gr.aueb.finalProject.controller;

import gr.aueb.finalProject.model.User;
import gr.aueb.finalProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Get statistics or data for the admin panel
        List<User> allUsers = userService.findAll();
        long totalUsers = allUsers.size();
        long adminUsers = allUsers.stream().filter(user -> user.getRole().equals("ROLE_ADMIN")).count();
        long studentUsers = allUsers.stream().filter(user -> user.getRole().equals("ROLE_STUDENT")).count();

        model.addAttribute("currentUser", username);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("studentUsers", studentUsers);
        model.addAttribute("allUsers", allUsers);

        return "admin-panel";
    }
}
