package kg.nurtelecom.o_subscriber_service.controller;

import kg.nurtelecom.o_subscriber_service.dto.BalanceUpdateRequest;
import kg.nurtelecom.o_subscriber_service.dto.EmailUpdateRequest;
import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.dto.TariffUpdateRequest;
import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import kg.nurtelecom.o_subscriber_service.entity.TariffPlan;
import kg.nurtelecom.o_subscriber_service.repository.AppUserRepository;
import kg.nurtelecom.o_subscriber_service.service.SubscriberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SubscriberViewController {

    private final SubscriberService subscriberService;
    private final AppUserRepository appUserRepository;

    public SubscriberViewController(SubscriberService subscriberService,
                                    AppUserRepository appUserRepository) {
        this.subscriberService = subscriberService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/subscribers-page")
    public String subscribersPage(Model model) {
        List<SubscriberResponse> subscribers = subscriberService.getAllSubscribers();
        model.addAttribute("subscribers", subscribers);
        return "subscribers-page";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        String username = authentication.getName();

        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (appUser.getSubscriber() == null) {
            if (isAdmin(authentication)) {
                return "redirect:/subscribers-page";
            }
            return "redirect:/home";
        }

        Long subscriberId = appUser.getSubscriber().getId();
        SubscriberResponse subscriber = subscriberService.getSubscriberById(subscriberId);

        model.addAttribute("subscriber", subscriber);
        model.addAttribute("emailForm", new EmailUpdateRequest());

        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String profilePageById(@PathVariable Long id, Model model) {
        SubscriberResponse subscriber = subscriberService.getSubscriberById(id);
        model.addAttribute("subscriber", subscriber);
        model.addAttribute("emailForm", new EmailUpdateRequest());
        return "profile";
    }

    @PostMapping("/subscribers/{id}/photo")
    public String uploadSubscriberPhoto(@PathVariable Long id,
                                        @RequestParam("photo") MultipartFile photo,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        try {
            subscriberService.uploadPhoto(id, photo);
            redirectAttributes.addFlashAttribute("successMessage", "Фотография успешно загружена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return buildProfileRedirect(authentication, id);
    }

    @PostMapping("/subscribers/{id}/photo/delete")
    public String deleteSubscriberPhoto(@PathVariable Long id,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        try {
            subscriberService.deletePhoto(id);
            redirectAttributes.addFlashAttribute("successMessage", "Фотография удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return buildProfileRedirect(authentication, id);
    }

    @PostMapping("/subscribers/{id}/email")
    public String updateSubscriberEmail(@PathVariable Long id,
                                        @RequestParam("email") String email,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        try {
            EmailUpdateRequest request = new EmailUpdateRequest();
            request.setEmail(email);
            subscriberService.updateEmailJdbcTemplate(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Email успешно сохранён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return buildProfileRedirect(authentication, id);
    }

    @PostMapping("/subscribers/{id}/tariff")
    public String updateSubscriberTariff(@PathVariable Long id,
                                         @RequestParam("tariffPlan") String tariffPlan,
                                         Authentication authentication,
                                         RedirectAttributes redirectAttributes) {
        try {
            TariffUpdateRequest request = new TariffUpdateRequest();
            request.setTariffPlan(TariffPlan.valueOf(tariffPlan));

            subscriberService.updateTariff(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Тариф успешно обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return buildProfileRedirect(authentication, id);
    }

    @PostMapping("/subscribers/{id}/balance/top-up")
    public String topUpBalance(@PathVariable Long id,
                               @RequestParam("amount") BigDecimal amount,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Сумма пополнения должна быть больше нуля");
            }

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(amount);

            subscriberService.updateBalance(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Баланс успешно пополнен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return buildProfileRedirect(authentication, id);
    }

    @PostMapping("/subscribers/{id}/toggle-status")
    public String toggleSubscriberStatus(@PathVariable Long id,
                                         RedirectAttributes redirectAttributes) {
        try {
            subscriberService.toggleActive(id);
            redirectAttributes.addFlashAttribute("successMessage", "Статус абонента успешно изменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/subscribers-page";
    }

    private String buildProfileRedirect(Authentication authentication, Long subscriberId) {
        if (isAdmin(authentication)) {
            return "redirect:/profile/" + subscriberId;
        }
        return "redirect:/profile";
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
}