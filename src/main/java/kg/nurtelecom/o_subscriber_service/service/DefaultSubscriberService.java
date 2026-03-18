package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.component.PhoneNormalizationComponent;
import kg.nurtelecom.o_subscriber_service.component.SubscriberMapper;
import kg.nurtelecom.o_subscriber_service.dto.*;
import kg.nurtelecom.o_subscriber_service.entity.Subscriber;
import kg.nurtelecom.o_subscriber_service.entity.TariffPlan;
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

import static kg.nurtelecom.o_subscriber_service.entity.TariffPlan.*;

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
            throw new DuplicateResourceException("Абонент с таким номером телефона уже существует");
        }

        if (!subscriber.getEmail().equals(request.getEmail())
                && subscriberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Абонент с таким email уже существует");
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

        TariffPlan newPlan = request.getTariffPlan();
        BigDecimal price = getTariffPrice(newPlan);
        Integer trafficGb = getTariffTrafficGb(newPlan);

        if (subscriber.getBalance().compareTo(price) < 0) {
            throw new IllegalArgumentException("Недостаточно средств для смены тарифа");
        }

        subscriber.setBalance(subscriber.getBalance().subtract(price));
        subscriber.setTariffPlan(newPlan);
        subscriber.setRemainingTrafficGb(trafficGb);
        subscriber.setTariffExpirationDate(java.time.LocalDate.now().plusDays(30));

        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(updatedSubscriber);
    }

    private BigDecimal getTariffPrice(TariffPlan tariffPlan) {
        return switch (tariffPlan) {
            case DIRECTOR -> new BigDecimal("1000");
            case MANAGER -> new BigDecimal("500");
            case SPECIALIST -> new BigDecimal("350");
        };
    }

    private Integer getTariffTrafficGb(TariffPlan tariffPlan) {
        return switch (tariffPlan) {
            case DIRECTOR -> null; // безлимит
            case MANAGER -> 50;
            case SPECIALIST -> 50;
        };
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
                "Фотография успешно загружена"
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
            throw new ResourceNotFoundException("Абонент не найден с id: " + id);
        }
    }

    @Override
    public void toggleActive(Long id) {
        Subscriber subscriber = findSubscriberOrThrow(id);
        subscriber.setActive(!subscriber.isActive());
        subscriberRepository.save(subscriber);
    }

    @Override
    @Transactional(readOnly = true)
    public String getEmailByIdJdbcOperations(Long id) {
        return subscriberJdbcDao.findEmailByIdJdbcOperations(id);
    }

    @Override
    public void updateEmailJdbcTemplate(Long id, EmailUpdateRequest request) {
        Subscriber subscriber = findSubscriberOrThrow(id);

        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email не должен быть пустым");
        }

        if (subscriber.getEmail() != null && subscriber.getEmail().equalsIgnoreCase(email)) {
            return;
        }

        if (subscriberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Абонент с таким email уже существует");
        }

        int updatedRows = subscriberJdbcDao.updateEmailJdbcTemplate(id, email);

        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Абонент не найден с id: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriberSummaryResponse> getSubscribersWithoutEmailJdbcClient() {
        return subscriberJdbcDao.findSubscribersWithoutEmailJdbcClient();
    }

    @Override
    public void deletePhoto(Long id) {
        Subscriber subscriber = findSubscriberOrThrow(id);

        String currentPhotoPath = subscriber.getPhotoPath();

        if (currentPhotoPath != null && !currentPhotoPath.isBlank()) {
            fileStorageService.deleteFile(currentPhotoPath);
            subscriber.setPhotoPath(null);
            subscriberRepository.save(subscriber);
        }
    }
}