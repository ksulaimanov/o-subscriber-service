package kg.nurtelecom.o_subscriber_service.dto;

import jakarta.validation.constraints.NotNull;
import kg.nurtelecom.o_subscriber_service.entity.TariffPlan;

public class TariffUpdateRequest {

    @NotNull(message = "Tariff plan is required")
    private TariffPlan tariffPlan;

    public TariffUpdateRequest() {
    }

    public TariffPlan getTariffPlan() {
        return tariffPlan;
    }

    public void setTariffPlan(TariffPlan tariffPlan) {
        this.tariffPlan = tariffPlan;
    }
}