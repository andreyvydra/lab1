package itmo.is.lab1.services.user.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpRequest {
    @NotNull(message = "Username не может быть null.")
    @NotBlank(message = "Username не может быть пустым.")
    @Size(min = 4, max = 255, message = "Длина username от 4 до 255.")
    private String username;

    @NotNull(message = "password не может быть null.")
    @NotBlank(message = "password не может быть пустым.")
    @Size(max = 255, message = "Длина password должна быть не более 255 символов.")
    private String password;
}
