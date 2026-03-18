package kg.nurtelecom.o_subscriber_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailUpdateRequest {

    @NotBlank(message = "Введите email")
    @Email(message = "Введите корректный email")
    private String email;

    public EmailUpdateRequest() {
    }

    public EmailUpdateRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}