package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final MenuService menuService;

    // Temporary endpoint to create a restaurant for testing
    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return menuService.createRestaurant(restaurant);
    }
}