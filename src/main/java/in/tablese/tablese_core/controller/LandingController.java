package in.tablese.tablese_core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @GetMapping("/")
    public String showLandingPage() {
        return "index"; // This will look for 'index.html' in /templates
    }
}