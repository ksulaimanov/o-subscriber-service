package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import kg.nurtelecom.o_subscriber_service.exception.DuplicateResourceException;
import kg.nurtelecom.o_subscriber_service.exception.ResourceNotFoundException;
import kg.nurtelecom.o_subscriber_service.repository.SubscriberRepository;

public abstract class AbstractSubscriberService {

    protected final SubscriberRepository subscriberRepository;

    protected AbstractSubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    protected Subscriber findSubscriberOrThrow(Long id) {
        return subscriberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found with id: " + id));
    }

    protected void validateUniqueFields(String phoneNumber, String email) {
        if (subscriberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicateResourceException("Subscriber with this phone number already exists");
        }
        if (subscriberRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Subscriber with this email already exists");
        }
    }
}