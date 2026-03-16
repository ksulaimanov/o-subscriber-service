package kg.nurtelecom.o_subscriber_service.component;

import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class SubscriberMapper {

    public SubscriberResponse toResponse(Subscriber subscriber) {
        return new SubscriberResponse(
                subscriber.getId(),
                subscriber.getFullName(),
                subscriber.getPhoneNumber(),
                subscriber.getEmail(),
                subscriber.getTariffPlan(),
                subscriber.getBalance(),
                subscriber.isActive(),
                subscriber.getPhotoPath()
        );
    }
}