package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.CreateOrUpdateMenuItemRequest;
import in.tablese.tablese_core.dto.MenuItemDto;
import in.tablese.tablese_core.mapper.MenuItemMapper;
import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/menu-items")
@RequiredArgsConstructor
public class AdminMenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuItemDto> addMenuItem(@Valid @RequestBody CreateOrUpdateMenuItemRequest request) {
        MenuItem menuItemToCreate = MenuItemMapper.toEntity(request);
        MenuItem createdMenuItem = menuService.addMenuItem(request.restaurantId(), menuItemToCreate);
        return new ResponseEntity<>(MenuItemMapper.toDto(createdMenuItem), HttpStatus.CREATED);
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long menuItemId, @Valid @RequestBody CreateOrUpdateMenuItemRequest request) {
        MenuItem menuItemToUpdate = MenuItemMapper.toEntity(request);
        MenuItem updatedMenuItem = menuService.updateMenuItem(menuItemId, menuItemToUpdate);
        return ResponseEntity.ok(MenuItemMapper.toDto(updatedMenuItem));
    }
}