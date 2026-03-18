package kg.nurtelecom.o_subscriber_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "tariff_plan", nullable = false)
    private TariffPlan tariffPlan;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "photo_path")
    private String photoPath;

    @OneToOne(mappedBy = "subscriber")
    private AppUser appUser;

    @Column(name = "remaining_traffic_gb")
    private Integer remainingTrafficGb;

    @Column(name = "tariff_expiration_date")
    private LocalDate tariffExpirationDate;
    private LocalDate packageExpiresAt;

    public Subscriber() {
    }


    public Subscriber(Long id, String fullName, String phoneNumber, String email, TariffPlan tariffPlan,
                      BigDecimal balance, boolean active, String photoPath, AppUser appUser,
                      Integer remainingTrafficGb, LocalDate tariffExpirationDate, LocalDate packageExpiresAt) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.tariffPlan = tariffPlan;
        this.balance = balance;
        this.active = active;
        this.photoPath = photoPath;
        this.appUser = appUser;
        this.remainingTrafficGb = remainingTrafficGb;
        this.tariffExpirationDate = tariffExpirationDate;
        this.packageExpiresAt = packageExpiresAt;
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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