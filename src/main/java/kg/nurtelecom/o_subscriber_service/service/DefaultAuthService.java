package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.dto.LoginRequest;
import kg.nurtelecom.o_subscriber_service.dto.LoginResponse;
import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import kg.nurtelecom.o_subscriber_service.repository.AppUserRepository;
import kg.nurtelecom.o_subscriber_service.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthService implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public DefaultAuthService(AuthenticationManager authenticationManager,
                              AppUserRepository appUserRepository,
                              JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AppUser appUser = appUserRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String token = jwtService.generateToken(appUser);

        return new LoginResponse(
                token,
                "Bearer",
                appUser.getUsername(),
                appUser.getRole().name()
        );
    }
}