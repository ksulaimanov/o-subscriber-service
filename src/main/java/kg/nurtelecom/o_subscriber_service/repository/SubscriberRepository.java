package kg.nurtelecom.o_subscriber_service.repository;

import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Optional<Subscriber> findByPhoneNumber(String phoneNumber);
    Optional<Subscriber> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}