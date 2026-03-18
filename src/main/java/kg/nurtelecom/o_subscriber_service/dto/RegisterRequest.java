package kg.nurtelecom.o_subscriber_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.nurtelecom.o_subscriber_service.validation.OPhoneNumber;

public class RegisterRequest {

    @NotBlank(message = "Введите ФИО")
    @Size(min = 2, max = 100, message = "ФИО должно содержать от 2 до 100 символов")
    private String fullName;

    @NotBlank(message = "Введите номер телефона")
    @OPhoneNumber
    private String phoneNumber;

    @NotBlank(message = "Введите логин")
    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Введите пароль")
    @Size(min = 6, max = 100, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    private String confirmPassword;

    public RegisterRequest() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}