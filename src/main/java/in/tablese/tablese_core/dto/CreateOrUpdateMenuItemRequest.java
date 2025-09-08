package in.tablese.tablese_core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// A record to represent the request payload for creating or updating a menu item.
public record CreateOrUpdateMenuItemRequest(
    
    @NotBlank(message = "Item name cannot be blank")
    @Size(min = 3, max = 100, message = "Item name must be between 3 and 100 characters")
    String name,
    
    @Size(max = 255, message = "Description cannot be more than 255 characters")
    String description,
    
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive value")
    Double price,

    @NotNull(message = "Availability must be specified")
    Boolean isAvailable

) {}