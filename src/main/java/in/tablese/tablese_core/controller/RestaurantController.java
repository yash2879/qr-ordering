package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.RestaurantDto;
import in.tablese.tablese_core.mapper.RestaurantMapper;
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

    @PostMapping
    public RestaurantDto createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = menuService.createRestaurant(restaurant);
        // Convert to DTO before sending the response
        return RestaurantMapper.toDto(createdRestaurant);
    }
}