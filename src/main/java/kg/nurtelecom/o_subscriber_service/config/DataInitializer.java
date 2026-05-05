package kg.nurtelecom.o_subscriber_service.config;

import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import kg.nurtelecom.o_subscriber_service.entity.Role;
import kg.nurtelecom.o_subscriber_service.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin", "admin123", Role.ROLE_ADMIN);
    }

    private void createUserIfNotExists(String username, String rawPassword, Role role) {
        if (appUserRepository.findByUsername(username).isEmpty()) {
            AppUser appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setPassword(passwordEncoder.encode(rawPassword));
            appUser.setRole(role);
            appUserRepository.save(appUser);
        }
    }
}