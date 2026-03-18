package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.dto.RegisterRequest;
import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import kg.nurtelecom.o_subscriber_service.entity.Role;
import kg.nurtelecom.o_subscriber_service.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultRegistrationService implements RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultRegistrationService(AppUserRepository appUserRepository,
                                      PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest request) {
        String username = request.getUsername() == null ? null : request.getUsername().trim();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Логин обязателен");
        }

        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(Role.ROLE_USER);

        appUserRepository.save(appUser);
    }
}