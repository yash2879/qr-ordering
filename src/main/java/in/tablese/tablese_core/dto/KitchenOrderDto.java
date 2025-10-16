package in.tablese.tablese_core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record KitchenOrderDto(
    Long id,
    String tableNumber,
    String status,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    LocalDateTime orderTime,
    List<KitchenOrderItemDto> items
) {}