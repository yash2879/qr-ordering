package in.tablese.tablese_core.dto;

import java.time.LocalDateTime;
import java.util.List;

public record KitchenOrderDto(
    Long id,
    String tableNumber,
    String status,
    LocalDateTime orderTime,
    List<KitchenOrderItemDto> items
) {}