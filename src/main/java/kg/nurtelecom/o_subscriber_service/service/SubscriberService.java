package kg.nurtelecom.o_subscriber_service.service;

import kg.nurtelecom.o_subscriber_service.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubscriberService {

    SubscriberResponse createSubscriber(CreateSubscriberRequest request);

    SubscriberResponse getSubscriberById(Long id);

    List<SubscriberResponse> getAllSubscribers();

    SubscriberResponse updateSubscriber(Long id, UpdateSubscriberRequest request);

    void deleteSubscriber(Long id);

    SubscriberResponse updateBalance(Long id, BalanceUpdateRequest request);

    SubscriberResponse updateTariff(Long id, TariffUpdateRequest request);

    PhotoUploadResponse uploadPhoto(Long id, MultipartFile file);

    int countSubscribersNativeJdbc();

    java.util.List<kg.nurtelecom.o_subscriber_service.dto.SubscriberSummaryResponse> getAllSummariesJdbcTemplate();

    kg.nurtelecom.o_subscriber_service.dto.SubscriberSummaryResponse getSummaryByIdJdbcOperations(Long id);

    java.util.List<kg.nurtelecom.o_subscriber_service.dto.SubscriberSummaryResponse> getSubscribersWithBalanceGreaterThanJdbcClient(java.math.BigDecimal amount);

    void deactivateSubscriberJdbcTemplate(Long id);

    void toggleActive(Long id);

    String getEmailByIdJdbcOperations(Long id);

    void updateEmailJdbcTemplate(Long id, EmailUpdateRequest request);

    java.util.List<kg.nurtelecom.o_subscriber_service.dto.SubscriberSummaryResponse> getSubscribersWithoutEmailJdbcClient();

    void deletePhoto(Long id);
}