package in.tablese.tablese_core.dto;

import java.time.LocalDateTime;
import java.util.List;

// Represents a complete order for API responses
public record CustomerOrderResponseDto(
    Long id,
    String tableNumber,
    String status,
    LocalDateTime orderTime,
    Long restaurantId,
    List<OrderItemResponseDto> items
) {}