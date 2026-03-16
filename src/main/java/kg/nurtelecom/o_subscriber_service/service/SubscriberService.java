package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.dto.*;

import java.util.List;

public interface SubscriberService {

    SubscriberResponse createSubscriber(CreateSubscriberRequest request);

    SubscriberResponse getSubscriberById(Long id);

    List<SubscriberResponse> getAllSubscribers();

    SubscriberResponse updateSubscriber(Long id, UpdateSubscriberRequest request);

    void deleteSubscriber(Long id);

    SubscriberResponse updateBalance(Long id, BalanceUpdateRequest request);

    SubscriberResponse updateTariff(Long id, TariffUpdateRequest request);
}