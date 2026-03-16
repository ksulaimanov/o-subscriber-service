package kg.nurtelecom.o_subscriber_service.dto;

import java.math.BigDecimal;

public class SubscriberSummaryResponse {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private BigDecimal balance;
    private String tariffPlan;

    public SubscriberSummaryResponse() {
    }

    public SubscriberSummaryResponse(Long id, String fullName, String phoneNumber, BigDecimal balance, String tariffPlan) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.tariffPlan = tariffPlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getTariffPlan() {
        return tariffPlan;
    }

    public void setTariffPlan(String tariffPlan) {
        this.tariffPlan = tariffPlan;
    }
}