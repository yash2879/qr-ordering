package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.service.CustomUserDetails;
import in.tablese.tablese_core.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final MenuService menuService;

    /**
     * Handles GET requests to the admin dashboard.
     * Retrieves the full menu for the logged-in user's restaurant and adds it to the model.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @param currentUser the currently authenticated user
     * @return the view name for the admin dashboard
     */
    @GetMapping
    public String showDashboard(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("username", currentUser.getUsername());
        return "admin/dashboard";
    }

    @GetMapping("/menu-management")
    public String showMenuManagement(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        Long restaurantId = currentUser.getRestaurantId(); // Get ID from logged-in user
        List<MenuItem> menuItems = menuService.getFullMenu(restaurantId);
        model.addAttribute("menuItems", menuItems);
        return "admin/menu-management";
    }

    /**
     * Handles GET requests to show the form for adding a new menu item.
     * Adds an empty MenuItem object to the model for form data binding.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the view name for the add menu item form
     */
    @GetMapping("/menu/new")
    public String showAddMenuItemForm(Model model) {
        // We add an empty MenuItem object to the model.
        // This object will be used by the form for data binding.
        model.addAttribute("menuItem", new MenuItem());
        return "admin/menu-form";
    }

    /**
     * Handles GET requests to show the form for editing an existing menu item.
     * Fetches the menu item by its ID and adds it to the model for form data binding.
     *
     * @param id the ID of the menu item to edit
     * @param model the Spring MVC model to pass attributes to the view
     * @return the view name for the edit menu item form
     *  */
    @GetMapping("/menu/edit/{id}")
    public String showEditMenuItemForm(@PathVariable Long id, Model model) {
        // Fetch the existing menu item from the database
        MenuItem menuItem = menuService.getMenuItemById(id);
        // Add it to the model to pre-populate the form fields
        model.addAttribute("menuItem", menuItem);
        return "admin/menu-form";
    }

    /**
     * Handles POST requests to save a new or updated menu item.
     * Uses @ModelAttribute to bind form fields to a MenuItem object.
     *
     * @param menuItem the MenuItem object populated from the form
     * @param currentUser the currently authenticated user
     * @return a redirect to the admin dashboard after saving
     */
    @PostMapping("/menu/save")
    public String saveMenuItem(@ModelAttribute("menuItem") MenuItem menuItem,
                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        // The @ModelAttribute annotation automatically binds the form fields
        // to the properties of the menuItem object.
        Long restaurantId = currentUser.getRestaurantId(); // Get ID from logged-in user

        if (menuItem.getId() == null) {
            // If the ID is null, it's a new item
            menuService.addMenuItem(restaurantId, menuItem);
        } else {
            // Otherwise, it's an existing item to be updated
            menuService.updateMenuItem(menuItem.getId(), menuItem);
        }

        // Redirect back to the main dashboard page after saving
        return "redirect:/admin/dashboard";
    }
}