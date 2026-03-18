package kg.nurtelecom.o_subscriber_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriberResponse {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String tariffPlan;
    private BigDecimal balance;
    private boolean active;
    private String photoPath;
    private Integer remainingTrafficGb;
    private LocalDate tariffExpirationDate;

    public SubscriberResponse() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTariffPlan() {
        return tariffPlan;
    }

    public void setTariffPlan(String tariffPlan) {
        this.tariffPlan = tariffPlan;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getRemainingTrafficGb() {
        return remainingTrafficGb;
    }

    public void setRemainingTrafficGb(Integer remainingTrafficGb) {
        this.remainingTrafficGb = remainingTrafficGb;
    }

    public LocalDate getTariffExpirationDate() {
        return tariffExpirationDate;
    }

    public void setTariffExpirationDate(LocalDate tariffExpirationDate) {
        this.tariffExpirationDate = tariffExpirationDate;
    }
}