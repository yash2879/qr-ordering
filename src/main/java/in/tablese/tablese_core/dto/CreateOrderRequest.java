package in.tablese.tablese_core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
    @NotNull Long restaurantId,
    @NotBlank String tableNumber,
    @NotEmpty @Valid List<OrderItemRequest> items
) {}