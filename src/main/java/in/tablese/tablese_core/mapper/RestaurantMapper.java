package in.tablese.tablese_core.mapper;

import in.tablese.tablese_core.dto.RestaurantDto;
import in.tablese.tablese_core.model.Restaurant;

public class RestaurantMapper {
    public static RestaurantDto toDto(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress()
        );
    }
}