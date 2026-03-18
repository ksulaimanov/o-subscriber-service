package kg.nurtelecom.o_subscriber_service.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.o_subscriber_service.dto.BalanceUpdateRequest;
import kg.nurtelecom.o_subscriber_service.dto.CreateSubscriberRequest;
import kg.nurtelecom.o_subscriber_service.dto.PhotoUploadResponse;
import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.dto.SubscriberSummaryResponse;
import kg.nurtelecom.o_subscriber_service.dto.TariffUpdateRequest;
import kg.nurtelecom.o_subscriber_service.dto.UpdateSubscriberRequest;
import kg.nurtelecom.o_subscriber_service.service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponse> createSubscriber(@Valid @RequestBody CreateSubscriberRequest request) {
        SubscriberResponse response = subscriberService.createSubscriber(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponse> getSubscriberById(@PathVariable Long id) {
        SubscriberResponse response = subscriberService.getSubscriberById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriberResponse>> getAllSubscribers() {
        List<SubscriberResponse> response = subscriberService.getAllSubscribers();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriberResponse> updateSubscriber(@PathVariable Long id,
                                                               @Valid @RequestBody UpdateSubscriberRequest request) {
        SubscriberResponse response = subscriberService.updateSubscriber(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        subscriberService.deleteSubscriber(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/balance")
    public ResponseEntity<SubscriberResponse> updateBalance(@PathVariable Long id,
                                                            @Valid @RequestBody BalanceUpdateRequest request) {
        SubscriberResponse response = subscriberService.updateBalance(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/tariff")
    public ResponseEntity<SubscriberResponse> updateTariff(@PathVariable Long id,
                                                           @Valid @RequestBody TariffUpdateRequest request) {
        SubscriberResponse response = subscriberService.updateTariff(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<PhotoUploadResponse> uploadPhoto(@PathVariable Long id,
                                                           @RequestParam("file") MultipartFile file) {
        PhotoUploadResponse response = subscriberService.uploadPhoto(id, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dao/count")
    public ResponseEntity<Integer> countSubscribersNativeJdbc() {
        return ResponseEntity.ok(subscriberService.countSubscribersNativeJdbc());
    }

    @GetMapping("/dao/summaries")
    public ResponseEntity<List<SubscriberSummaryResponse>> getAllSummariesJdbcTemplate() {
        return ResponseEntity.ok(subscriberService.getAllSummariesJdbcTemplate());
    }

    @GetMapping("/dao/summaries/{id}")
    public ResponseEntity<SubscriberSummaryResponse> getSummaryByIdJdbcOperations(@PathVariable Long id) {
        return ResponseEntity.ok(subscriberService.getSummaryByIdJdbcOperations(id));
    }

    @GetMapping("/dao/filter-by-balance")
    public ResponseEntity<List<SubscriberSummaryResponse>> getSubscribersWithBalanceGreaterThanJdbcClient(
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(subscriberService.getSubscribersWithBalanceGreaterThanJdbcClient(amount));
    }

    @PatchMapping("/dao/{id}/deactivate")
    public ResponseEntity<String> deactivateSubscriberJdbcTemplate(@PathVariable Long id) {
        subscriberService.deactivateSubscriberJdbcTemplate(id);
        return ResponseEntity.ok("Абонент успешно деактивирован");
    }

    @PostMapping("/{id}/toggle")
    public ResponseEntity<String> toggleSubscriberStatus(@PathVariable Long id) {
        subscriberService.toggleActive(id);
        return ResponseEntity.ok("Статус абонента успешно изменён");
    }
}