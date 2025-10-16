package in.tablese.tablese_core.service;

import in.tablese.tablese_core.exception.ResourceNotFoundException;
import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.repository.MenuItemRepository;
import in.tablese.tablese_core.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // Lombok annotation for constructor injection
@Transactional(readOnly = true) // All methods are read-only by default
public class MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    // Method to create a new restaurant
    @Transactional // Override default to allow writes
    public Restaurant createRestaurant(Restaurant restaurant) {
        log.info("Creating new restaurant: {}", restaurant.getName());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        log.info("Successfully created restaurant with ID: {}", savedRestaurant.getId());
        return savedRestaurant;
    }

    // Method to add a menu item to a specific restaurant
    @Transactional
    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem) {
        log.info("Adding new menu item '{}' to restaurant ID: {}", menuItem.getName(), restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.error("Failed to add menu item: Restaurant not found with ID: {}", restaurantId);
                    return new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
                });
        menuItem.setRestaurant(restaurant);
        MenuItem savedItem = menuItemRepository.save(menuItem);
        log.info("Successfully added menu item '{}' with ID: {} to restaurant: {}",
                savedItem.getName(), savedItem.getId(), restaurant.getName());
        return savedItem;
    }

    // Method to get the full menu for a specific restaurant
    public List<MenuItem> getFullMenu(Long restaurantId) {
        log.debug("Fetching active menu items for restaurant ID: {}", restaurantId);
        List<MenuItem> menu = menuItemRepository.findByRestaurantIdAndIsActiveTrue(restaurantId);
        log.debug("Found {} active menu items for restaurant ID: {}", menu.size(), restaurantId);
        return menu;
    }

    public List<MenuItem> findAllByRestaurantId(Long restaurantId) {
        log.debug("Fetching all menu items for restaurant ID: {}", restaurantId);
        List<MenuItem> items = menuItemRepository.findAllByRestaurantId(restaurantId);
        log.debug("Found {} total menu items for restaurant ID: {}", items.size(), restaurantId);
        return items;
    }
    
    // Method to get a single menu item by its ID
    public MenuItem getMenuItemById(Long menuItemId) {
        log.debug("Fetching menu item with ID: {}", menuItemId);
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> {
                    log.error("Menu item not found with ID: {}", menuItemId);
                    return new ResourceNotFoundException("Menu item not found with id: " + menuItemId);
                });
    }

    // Method to update an existing menu item
    @Transactional
    public MenuItem updateMenuItem(Long menuItemId, MenuItem updatedMenuItemDetails) {
        log.info("Updating menu item ID: {} with new details", menuItemId);
        MenuItem existingMenuItem = getMenuItemById(menuItemId);
        
        log.debug("Updating menu item details - Name: {}, Price: {}, Available: {}",
                updatedMenuItemDetails.getName(),
                updatedMenuItemDetails.getPrice(),
                updatedMenuItemDetails.isAvailable());

        existingMenuItem.setName(updatedMenuItemDetails.getName());
        existingMenuItem.setDescription(updatedMenuItemDetails.getDescription());
        existingMenuItem.setPrice(updatedMenuItemDetails.getPrice());
        existingMenuItem.setAvailable(updatedMenuItemDetails.isAvailable());

        MenuItem updatedItem = menuItemRepository.save(existingMenuItem);
        log.info("Successfully updated menu item ID: {}", updatedItem.getId());
        return updatedItem;
    }

    @Transactional
    public void deleteMenuItem(Long menuItemId) {
        log.info("Soft-deleting menu item with ID: {}", menuItemId);
        MenuItem item = getMenuItemById(menuItemId);
        item.setActive(false);
        menuItemRepository.save(item);
        log.info("Successfully soft-deleted menu item ID: {}", menuItemId);
    }

    @Transactional
    public void updateAvailability(Long restaurantId, Long menuId, boolean available) {
        log.info("Updating availability for menu item ID: {} in restaurant ID: {} to: {}",
                menuId, restaurantId, available);

        MenuItem menuItem = menuItemRepository.findById(menuId)
                .orElseThrow(() -> {
                    log.error("Failed to update availability: Menu item not found with ID: {}", menuId);
                    return new ResourceNotFoundException("Menu item not found with id: " + menuId);
                });

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            log.error("Menu item ID: {} does not belong to restaurant ID: {}", menuId, restaurantId);
            throw new ResourceNotFoundException("Menu item with id: " + menuId +
                    " does not belong to restaurant with id: " + restaurantId);
        }

        menuItem.setAvailable(available);
        menuItemRepository.save(menuItem);
        log.info("Successfully updated availability of menu item ID: {} to: {}", menuId, available);
    }
}