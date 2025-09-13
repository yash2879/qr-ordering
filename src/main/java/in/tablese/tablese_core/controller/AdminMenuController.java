package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.AvailabilityUpdateRequest;
import in.tablese.tablese_core.dto.CreateOrUpdateMenuItemRequest;
import in.tablese.tablese_core.dto.MenuItemDto;
import in.tablese.tablese_core.exception.ResourceNotFoundException;
import in.tablese.tablese_core.mapper.MenuItemMapper;
import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.service.CustomUserDetails;
import in.tablese.tablese_core.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/menu-items")
@RequiredArgsConstructor
public class AdminMenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuItemDto> addMenuItem(@Valid @RequestBody CreateOrUpdateMenuItemRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails currentUser) {
        Long restaurantId = currentUser.getRestaurantId();
        MenuItem menuItemToCreate = MenuItemMapper.toEntity(request);
        MenuItem createdMenuItem = menuService.addMenuItem(restaurantId, menuItemToCreate);
        return new ResponseEntity<>(MenuItemMapper.toDto(createdMenuItem), HttpStatus.CREATED);
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long menuItemId, @Valid @RequestBody CreateOrUpdateMenuItemRequest request) {
        MenuItem menuItemToUpdate = MenuItemMapper.toEntity(request);
        MenuItem updatedMenuItem = menuService.updateMenuItem(menuItemId, menuItemToUpdate);
        return ResponseEntity.ok(MenuItemMapper.toDto(updatedMenuItem));
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long menuItemId) {
        menuService.deleteMenuItem(menuItemId);

        // A 204 No Content response is a standard and appropriate response for a successful deletion.
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestBody AvailabilityUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) { // <-- INJECT THE LOGGED-IN USER

        // 1. Check if a user is logged in. If not, something is wrong with security config.
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Get the restaurant ID directly and securely from the logged-in user's details.
        Long restaurantId = currentUser.getRestaurantId();

        // 3. Pass all necessary, trusted information to the service layer.
        // The service should still verify that the menu item `id` belongs to `restaurantId`
        // as a final layer of protection.
        try {
            menuService.updateAvailability(restaurantId, id, request.isAvailable());
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            // Handle cases where the item doesn't exist or doesn't belong to the restaurant
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}