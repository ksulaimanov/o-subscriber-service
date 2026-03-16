package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.component.PhoneNormalizationComponent;
import kg.nurtelecom.o_subscriber_service.component.SubscriberMapper;
import kg.nurtelecom.o_subscriber_service.dto.*;
import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import kg.nurtelecom.o_subscriber_service.exception.DuplicateResourceException;
import kg.nurtelecom.o_subscriber_service.exception.ResourceNotFoundException;
import kg.nurtelecom.o_subscriber_service.filesystem.FileStorageService;
import kg.nurtelecom.o_subscriber_service.repository.SubscriberJdbcDao;
import kg.nurtelecom.o_subscriber_service.repository.SubscriberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DefaultSubscriberService extends AbstractSubscriberService implements SubscriberService {

    private final SubscriberMapper subscriberMapper;
    private final FileStorageService fileStorageService;
    private final SubscriberJdbcDao subscriberJdbcDao;
    private final PhoneNormalizationComponent phoneNormalizationComponent;

    public DefaultSubscriberService(SubscriberRepository subscriberRepository,
                                    SubscriberMapper subscriberMapper,
                                    FileStorageService fileStorageService,
                                    SubscriberJdbcDao subscriberJdbcDao,
                                    PhoneNormalizationComponent phoneNormalizationComponent) {
        super(subscriberRepository);
        this.subscriberMapper = subscriberMapper;
        this.fileStorageService = fileStorageService;
        this.subscriberJdbcDao = subscriberJdbcDao;
        this.phoneNormalizationComponent = phoneNormalizationComponent;
    }

    @Override
    public SubscriberResponse createSubscriber(CreateSubscriberRequest request) {
        String normalizedPhone = phoneNormalizationComponent.normalize(request.getPhoneNumber());
        validateUniqueFields(normalizedPhone, request.getEmail());

        Subscriber subscriber = new Subscriber();
        subscriber.setFullName(request.getFullName());
        subscriber.setPhoneNumber(normalizedPhone);
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
        String normalizedPhone = phoneNormalizationComponent.normalize(request.getPhoneNumber());

        if (!subscriber.getPhoneNumber().equals(normalizedPhone)
                && subscriberRepository.existsByPhoneNumber(normalizedPhone)) {
            throw new DuplicateResourceException("Subscriber with this phone number already exists");
        }

        if (!subscriber.getEmail().equals(request.getEmail())
                && subscriberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Subscriber with this email already exists");
        }

        subscriber.setFullName(request.getFullName());
        subscriber.setPhoneNumber(normalizedPhone);
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

    @Override
    public PhotoUploadResponse uploadPhoto(Long id, MultipartFile file) {
        Subscriber subscriber = findSubscriberOrThrow(id);

        String savedPath = fileStorageService.saveFile(file);
        subscriber.setPhotoPath(savedPath);

        subscriberRepository.save(subscriber);

        return new PhotoUploadResponse(
                subscriber.getId(),
                subscriber.getPhotoPath(),
                "Photo uploaded successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public int countSubscribersNativeJdbc() {
        return subscriberJdbcDao.countSubscribersNativeJdbc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriberSummaryResponse> getAllSummariesJdbcTemplate() {
        return subscriberJdbcDao.findAllSummariesJdbcTemplate();
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriberSummaryResponse getSummaryByIdJdbcOperations(Long id) {
        return subscriberJdbcDao.findSummaryByIdJdbcOperations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriberSummaryResponse> getSubscribersWithBalanceGreaterThanJdbcClient(BigDecimal amount) {
        return subscriberJdbcDao.findSubscribersWithBalanceGreaterThanJdbcClient(amount);
    }

    @Override
    public void deactivateSubscriberJdbcTemplate(Long id) {
        int updatedRows = subscriberJdbcDao.deactivateSubscriberJdbcTemplate(id);
        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Subscriber not found with id: " + id);
        }
    }
}