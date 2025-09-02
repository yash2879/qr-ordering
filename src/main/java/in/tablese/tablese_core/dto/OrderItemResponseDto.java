package in.tablese.tablese_core.dto;

// Represents a single item within an order response
public record OrderItemResponseDto(
    Integer quantity,
    String itemName,
    Double price
) {}