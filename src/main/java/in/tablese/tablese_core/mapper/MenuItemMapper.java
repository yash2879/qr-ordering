package in.tablese.tablese_core.mapper;

import in.tablese.tablese_core.dto.CreateOrUpdateMenuItemRequest;
import in.tablese.tablese_core.dto.MenuItemDto;
import in.tablese.tablese_core.model.MenuItem;

public class MenuItemMapper {

    // Converts a MenuItem entity to a MenuItemDto for sending to the client
    public static MenuItemDto toDto(MenuItem menuItem) {
        return new MenuItemDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isAvailable()
        );
    }

    // Converts a CreateOrUpdateMenuItemRequest DTO to a MenuItem entity for saving
    public static MenuItem toEntity(CreateOrUpdateMenuItemRequest request) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.name());
        menuItem.setDescription(request.description());
        menuItem.setPrice(request.price());
        menuItem.setAvailable(request.isAvailable());
        // Note: The restaurant is not set here. It will be set in the service layer.
        return menuItem;
    }
}