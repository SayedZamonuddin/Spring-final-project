package org.ucentralasia.NovelNest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token is mandatory")
    private String token;

    @NotBlank(message = "New password is mandatory")
    @Size(min=8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
