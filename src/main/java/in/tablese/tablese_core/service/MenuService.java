package in.tablese.tablese_core.service;

import in.tablese.tablese_core.exception.ResourceNotFoundException;
import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.repository.MenuItemRepository;
import in.tablese.tablese_core.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok annotation for constructor injection
@Transactional(readOnly = true) // All methods are read-only by default
public class MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    // Method to create a new restaurant
    @Transactional // Override default to allow writes
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Method to add a menu item to a specific restaurant
    @Transactional
    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem) {
        logger.info("Attempting to add menu item '{}' to restaurant ID {}", menuItem.getName(), restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                // Use our custom exception
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        menuItem.setRestaurant(restaurant);
        MenuItem savedItem = menuItemRepository.save(menuItem);
        logger.info("Successfully saved new menu item with ID {}", savedItem.getId());
        return savedItem;
    }

    // Method to get the full menu for a specific restaurant
    public List<MenuItem> getFullMenu(Long restaurantId) {
        // This uses the custom method we defined in the repository
        return menuItemRepository.findByRestaurantIdAndIsActiveTrue(restaurantId);
    }
    
    // Method to get a single menu item by its ID
    public MenuItem getMenuItemById(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));
    }

    // Method to update an existing menu item
    @Transactional
    public MenuItem updateMenuItem(Long menuItemId, MenuItem updatedMenuItemDetails) {
        logger.info("Attempting to update menu item with ID {}", menuItemId);
        MenuItem existingMenuItem = getMenuItemById(menuItemId);
        
        existingMenuItem.setName(updatedMenuItemDetails.getName());
        existingMenuItem.setDescription(updatedMenuItemDetails.getDescription());
        existingMenuItem.setPrice(updatedMenuItemDetails.getPrice());
        existingMenuItem.setAvailable(updatedMenuItemDetails.isAvailable());

        MenuItem updatedItem = menuItemRepository.save(existingMenuItem);

        logger.info("Successfully updated menu item with ID {}", updatedItem.getId());
        return updatedItem;
    }

    @Transactional
    public void deleteMenuItem(Long menuItemId) { // This is now a "soft delete"
        logger.info("Attempting to soft-delete menu item with ID {}", menuItemId);
        MenuItem item = getMenuItemById(menuItemId); // Reuses our existing method
        item.setActive(false);
        menuItemRepository.save(item);
        logger.info("Successfully soft-deleted menu item with ID {}", menuItemId);
    }
}