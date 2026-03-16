package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.component.SubscriberMapper;
import kg.nurtelecom.o_subscriber_service.dto.*;
import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import kg.nurtelecom.o_subscriber_service.exception.DuplicateResourceException;
import kg.nurtelecom.o_subscriber_service.repository.SubscriberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultSubscriberService extends AbstractSubscriberService implements SubscriberService {

    private final SubscriberMapper subscriberMapper;

    public DefaultSubscriberService(SubscriberRepository subscriberRepository, SubscriberMapper subscriberMapper) {
        super(subscriberRepository);
        this.subscriberMapper = subscriberMapper;
    }

    @Override
    public SubscriberResponse createSubscriber(CreateSubscriberRequest request) {
        validateUniqueFields(request.getPhoneNumber(), request.getEmail());

        Subscriber subscriber = new Subscriber();
        subscriber.setFullName(request.getFullName());
        subscriber.setPhoneNumber(request.getPhoneNumber());
        subscriber.setEmail(request.getEmail());
        subscriber.setTariffPlan(request.getTariffPlan());
        subscriber.setBalance(request.getBalance());
        subscriber.setActive(request.getActive());
        subscriber.setPhotoPath(null);

        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(savedSubscriber);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriberResponse getSubscriberById(Long id) {
        Subscriber subscriber = findSubscriberOrThrow(id);
        return subscriberMapper.toResponse(subscriber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriberResponse> getAllSubscribers() {
        return subscriberRepository.findAll()
                .stream()
                .map(subscriberMapper::toResponse)
                .toList();
    }

    @Override
    public SubscriberResponse updateSubscriber(Long id, UpdateSubscriberRequest request) {
        Subscriber subscriber = findSubscriberOrThrow(id);

        if (!subscriber.getPhoneNumber().equals(request.getPhoneNumber())
                && subscriberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Subscriber with this phone number already exists");
        }

        if (!subscriber.getEmail().equals(request.getEmail())
                && subscriberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Subscriber with this email already exists");
        }

        subscriber.setFullName(request.getFullName());
        subscriber.setPhoneNumber(request.getPhoneNumber());
        subscriber.setEmail(request.getEmail());
        subscriber.setTariffPlan(request.getTariffPlan());
        subscriber.setBalance(request.getBalance());
        subscriber.setActive(request.getActive());

        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(updatedSubscriber);
    }

    @Override
    public void deleteSubscriber(Long id) {
        Subscriber subscriber = findSubscriberOrThrow(id);
        subscriberRepository.delete(subscriber);
    }

    @Override
    public SubscriberResponse updateBalance(Long id, BalanceUpdateRequest request) {
        Subscriber subscriber = findSubscriberOrThrow(id);
        subscriber.setBalance(subscriber.getBalance().add(request.getAmount()));

        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(updatedSubscriber);
    }

    @Override
    public SubscriberResponse updateTariff(Long id, TariffUpdateRequest request) {
        Subscriber subscriber = findSubscriberOrThrow(id);
        subscriber.setTariffPlan(request.getTariffPlan());

        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(updatedSubscriber);
    }
}