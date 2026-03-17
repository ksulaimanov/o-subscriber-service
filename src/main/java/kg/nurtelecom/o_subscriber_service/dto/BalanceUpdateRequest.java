package kg.nurtelecom.o_subscriber_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BalanceUpdateRequest {

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", inclusive = true, message = "Сумма должна быть больше 0")
    private BigDecimal amount;

    public BalanceUpdateRequest() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}