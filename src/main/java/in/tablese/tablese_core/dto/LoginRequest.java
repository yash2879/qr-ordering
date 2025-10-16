package in.tablese.tablese_core.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username cannot be blank")
    String username,
    
    @NotBlank(message = "Password cannot be blank")
    String password
) {}