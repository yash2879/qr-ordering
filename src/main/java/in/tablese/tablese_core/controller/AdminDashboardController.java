package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/menu/new")
    public String showAddMenuItemForm(Model model) {
        // We add an empty MenuItem object to the model.
        // This object will be used by the form for data binding.
        model.addAttribute("menuItem", new MenuItem());
        return "admin/menu-form";
    }

    @GetMapping("/menu/edit/{id}")
    public String showEditMenuItemForm(@PathVariable Long id, Model model) {
        // Fetch the existing menu item from the database
        MenuItem menuItem = menuService.getMenuItemById(id);
        // Add it to the model to pre-populate the form fields
        model.addAttribute("menuItem", menuItem);
        return "admin/menu-form";
    }

    @PostMapping("/menu/save")
    public String saveMenuItem(@ModelAttribute("menuItem") MenuItem menuItem) {
        // The @ModelAttribute annotation automatically binds the form fields
        // to the properties of the menuItem object.

        if (menuItem.getId() == null) {
            // If the ID is null, it's a new item
            menuService.addMenuItem(TEMP_RESTAURANT_ID, menuItem);
        } else {
            // Otherwise, it's an existing item to be updated
            menuService.updateMenuItem(menuItem.getId(), menuItem);
        }

        // Redirect back to the main dashboard page after saving
        return "redirect:/admin/dashboard";
    }
}