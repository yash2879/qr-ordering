package in.tablese.tablese_core.dto;

// Represents a restaurant for API responses
public record RestaurantDto(
    Long id,
    String name,
    String address
) {}