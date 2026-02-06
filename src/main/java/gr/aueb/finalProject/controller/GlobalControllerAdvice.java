package gr.aueb.finalProject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public String addCurrentUserToModel(Authentication authentication) {
        // This automatically adds "currentUser" to the model for all controllers
        return (authentication != null) ? authentication.getName() : null;
    }
}