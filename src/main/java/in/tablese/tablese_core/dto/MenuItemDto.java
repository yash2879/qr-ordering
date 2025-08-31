package in.tablese.tablese_core.dto;

// A record to represent a menu item for display purposes.
public record MenuItemDto(
    Long id,
    String name,
    String description,
    Double price,
    boolean isAvailable
) {}