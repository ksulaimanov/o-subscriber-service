package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.dto.RegisterRequest;

public interface RegistrationService {
    void register(RegisterRequest request);
}