package in.tablese.tablese_core.service;

import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.repository.MenuItemRepository;
import in.tablese.tablese_core.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok annotation for constructor injection
@Transactional(readOnly = true) // All methods are read-only by default
public class MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    // Method to create a new restaurant
    @Transactional // Override default to allow writes
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Method to add a menu item to a specific restaurant
    @Transactional // Override default to allow writes
    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        menuItem.setRestaurant(restaurant);
        return menuItemRepository.save(menuItem);
    }

    // Method to get the full menu for a specific restaurant
    public List<MenuItem> getFullMenu(Long restaurantId) {
        // This uses the custom method we defined in the repository
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
    
    // Method to get a single menu item by its ID
    public MenuItem getMenuItemById(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + menuItemId));
    }

    // Method to update an existing menu item
    @Transactional
    public MenuItem updateMenuItem(Long menuItemId, MenuItem updatedMenuItemDetails) {
        MenuItem existingMenuItem = getMenuItemById(menuItemId);
        
        existingMenuItem.setName(updatedMenuItemDetails.getName());
        existingMenuItem.setDescription(updatedMenuItemDetails.getDescription());
        existingMenuItem.setPrice(updatedMenuItemDetails.getPrice());
        existingMenuItem.setAvailable(updatedMenuItemDetails.isAvailable());
        
        return menuItemRepository.save(existingMenuItem);
    }
}