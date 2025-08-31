package in.tablese.tablese_core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kitchen")
public class KitchenHubController {

    @GetMapping("/{restaurantId}")
    public String showKitchenHub(@PathVariable Long restaurantId, Model model) {
        // We pass the restaurantId to the model so the HTML/JavaScript can use it
        model.addAttribute("restaurantId", restaurantId);
        return "kitchen/hub"; // This will render hub.html
    }
}