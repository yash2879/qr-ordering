package in.tablese.tablese_core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
    @NotBlank @Size(min = 2)
    String ownerName, // We'll use this for the username for now

    @NotBlank @Email
    String ownerEmail, // Not used yet, but good to have

    @NotBlank @Size(min = 6, max = 100)
    String password,

    @NotBlank @Size(min = 3)
    String restaurantName,

    String restaurantAddress
) {}