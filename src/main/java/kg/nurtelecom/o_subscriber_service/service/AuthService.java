package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.dto.LoginRequest;
import kg.nurtelecom.o_subscriber_service.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}