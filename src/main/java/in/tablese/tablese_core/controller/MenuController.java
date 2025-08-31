package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.MenuItemDto;
import in.tablese.tablese_core.mapper.MenuItemMapper;
import in.tablese.tablese_core.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<MenuItemDto>> getMenuForRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemDto> menu = menuService.getFullMenu(restaurantId)
                .stream()
                .map(MenuItemMapper::toDto) // Convert each MenuItem to a MenuItemDto
                .collect(Collectors.toList());
        return ResponseEntity.ok(menu);
    }
}