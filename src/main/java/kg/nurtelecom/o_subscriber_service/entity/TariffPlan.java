package kg.nurtelecom.o_subscriber_service.entity;

import java.math.BigDecimal;

public enum TariffPlan {
    DIRECTOR("Director", new BigDecimal("1000"), "Unlimited Internet"),
    MANAGER("Manager", new BigDecimal("500"), "50 GB"),
    SPECIALIST("Specialist", new BigDecimal("350"), "50 GB");

    private final String title;
    private final BigDecimal monthlyFee;
    private final String internetPackage;

    TariffPlan(String title, BigDecimal monthlyFee, String internetPackage) {
        this.title = title;
        this.monthlyFee = monthlyFee;
        this.internetPackage = internetPackage;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public String getInternetPackage() {
        return internetPackage;
    }
}