package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.component.PhoneNormalizationComponent;
import kg.nurtelecom.o_subscriber_service.dto.RegisterRequest;
import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import kg.nurtelecom.o_subscriber_service.entity.Role;
import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import kg.nurtelecom.o_subscriber_service.entity.TariffPlan;
import kg.nurtelecom.o_subscriber_service.repository.AppUserRepository;
import kg.nurtelecom.o_subscriber_service.repository.SubscriberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class DefaultRegistrationService implements RegistrationService {

    private final AppUserRepository appUserRepository;
    private final SubscriberRepository subscriberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneNormalizationComponent phoneNormalizationComponent;

    public DefaultRegistrationService(AppUserRepository appUserRepository,
                                      SubscriberRepository subscriberRepository,
                                      PasswordEncoder passwordEncoder,
                                      PhoneNormalizationComponent phoneNormalizationComponent) {
        this.appUserRepository = appUserRepository;
        this.subscriberRepository = subscriberRepository;
        this.passwordEncoder = passwordEncoder;
        this.phoneNormalizationComponent = phoneNormalizationComponent;
    }

    @Override
    public void register(RegisterRequest request) {
        String username = request.getUsername() == null ? null : request.getUsername().trim();
        String fullName = request.getFullName() == null ? null : request.getFullName().trim();
        String normalizedPhone = phoneNormalizationComponent.normalize(request.getPhoneNumber());

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Логин обязателен");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО обязательно");
        }

        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Логин уже занят");
        }

        if (subscriberRepository.existsByPhoneNumber(normalizedPhone)) {
            throw new IllegalArgumentException("Этот номер уже зарегистрирован");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setFullName(fullName);
        subscriber.setPhoneNumber(normalizedPhone);
        subscriber.setBalance(BigDecimal.ZERO);
        subscriber.setActive(true);
        subscriber.setTariffPlan(TariffPlan.SPECIALIST);
        subscriber.setPhotoPath(null);

        Subscriber savedSubscriber = subscriberRepository.save(subscriber);

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(Role.ROLE_USER);
        appUser.setSubscriber(savedSubscriber);

        appUserRepository.save(appUser);
    }
}