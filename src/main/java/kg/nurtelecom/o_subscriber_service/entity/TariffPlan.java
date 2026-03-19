package kg.nurtelecom.o_subscriber_service.entity;

import java.math.BigDecimal;

public enum TariffPlan {
    DIRECTOR("DIRECTOR", new BigDecimal("1000"), null),
    MANAGER("MANAGER", new BigDecimal("500"), 50),
    SPECIALIST("SPECIALIST", new BigDecimal("350"), 50);

    private final String title;
    private final BigDecimal monthlyFee;
    private final Integer trafficGb;

    TariffPlan(String title, BigDecimal monthlyFee, Integer trafficGb) {
        this.title = title;
        this.monthlyFee = monthlyFee;
        this.trafficGb = trafficGb;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public Integer getTrafficGb() {
        return trafficGb;
    }

    public boolean isUnlimited() {
        return trafficGb == null;
    }
}