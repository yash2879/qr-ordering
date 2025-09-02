package in.tablese.tablese_core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
    @NotNull Long menuItemId,
    @NotNull @Min(1) Integer quantity
) {}