package in.tablese.tablese_core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerMenuController {

    @GetMapping("/menu/{restaurantId}")
    public String showCustomerMenu(
            @PathVariable Long restaurantId,
            @RequestParam(name = "table", required = false, defaultValue = "N/A") String tableNumber,
            Model model) {
        
        // Add the restaurantId and tableNumber to the model
        // so our HTML/JavaScript can access them.
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("tableNumber", tableNumber);
        
        // Return the name of our new customer menu HTML template
        return "customer/customer-menu";
    }
}