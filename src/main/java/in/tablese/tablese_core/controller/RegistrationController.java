package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.RegistrationRequest;
import in.tablese.tablese_core.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    // Method to SHOW the registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", "", "", ""));
        return "register";
    }

    // Method to PROCESS the registration form
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registrationRequest") RegistrationRequest request) {
        try {
            registrationService.registerNewRestaurant(request);
        } catch (IllegalStateException e) {
            // In a real app, you'd handle this more gracefully with a BindingResult
            // and show the error on the form. For now, we'll redirect back.
            return "redirect:/register?error";
        }
        // After successful registration, redirect to the login page with a success message
        return "redirect:/login?registered";
    }
}