package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final MenuService menuService;
    private static final Long TEMP_RESTAURANT_ID = 1L; // Hardcoded for now

    @GetMapping
    public String showDashboard(Model model) {
        // 1. Fetch the menu items from the service layer
        List<MenuItem> menuItems = menuService.getFullMenu(TEMP_RESTAURANT_ID);

        // 2. Add the list of menu items to the model
        model.addAttribute("menuItems", menuItems);
        
        // 3. Return the name of the HTML template to display
        return "admin/dashboard";
    }
}