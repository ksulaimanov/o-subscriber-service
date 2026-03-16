package kg.nurtelecom.o_subscriber_service.repository;

import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}