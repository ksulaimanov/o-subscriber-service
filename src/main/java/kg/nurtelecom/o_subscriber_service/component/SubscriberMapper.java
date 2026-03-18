package kg.nurtelecom.o_subscriber_service.component;

import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class SubscriberMapper {

    public SubscriberResponse toResponse(Subscriber subscriber) {
        SubscriberResponse response = new SubscriberResponse();
        response.setId(subscriber.getId());
        response.setFullName(subscriber.getFullName());
        response.setPhoneNumber(subscriber.getPhoneNumber());
        response.setEmail(subscriber.getEmail());
        response.setTariffPlan(subscriber.getTariffPlan() != null ? subscriber.getTariffPlan().name() : null);
        response.setBalance(subscriber.getBalance());
        response.setActive(subscriber.isActive());
        response.setPhotoPath(subscriber.getPhotoPath());
        response.setRemainingTrafficGb(subscriber.getRemainingTrafficGb());
        response.setTariffExpirationDate(subscriber.getTariffExpirationDate());
        return response;
    }
}