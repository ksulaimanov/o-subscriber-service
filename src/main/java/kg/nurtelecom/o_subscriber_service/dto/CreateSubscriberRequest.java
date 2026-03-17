package kg.nurtelecom.o_subscriber_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.nurtelecom.o_subscriber_service.entity.TariffPlan;
import kg.nurtelecom.o_subscriber_service.validation.OPhoneNumber;

import java.math.BigDecimal;

public class CreateSubscriberRequest {

    @NotBlank(message = "ФИО обязательно")
    private String fullName;

    @NotBlank(message = "Номер телефона обязателен")
    @OPhoneNumber
    private String phoneNumber;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotNull(message = "Тарифный план обязателен")
    private TariffPlan tariffPlan;

    @NotNull(message = "Баланс обязателен")
    @DecimalMin(value = "0.0", inclusive = true, message = "Баланс должен быть больше или равен 0")
    private BigDecimal balance;

    @NotNull(message = "Статус активности обязателен")
    private Boolean active;

    public CreateSubscriberRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TariffPlan getTariffPlan() {
        return tariffPlan;
    }

    public void setTariffPlan(TariffPlan tariffPlan) {
        this.tariffPlan = tariffPlan;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}