package org.ucentralasia.NovelNest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please provide a valid email")
    private String email;
}
