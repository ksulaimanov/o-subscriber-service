package kg.nurtelecom.o_subscriber_service.controller;

import kg.nurtelecom.o_subscriber_service.dto.EmailUpdateRequest;
import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.entity.AppUser;
import kg.nurtelecom.o_subscriber_service.repository.AppUserRepository;
import kg.nurtelecom.o_subscriber_service.service.SubscriberService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                        RedirectAttributes redirectAttributes) {
        try {
            subscriberService.uploadPhoto(id, photo);
            redirectAttributes.addFlashAttribute("successMessage", "Фотография успешно загружена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile/" + id;
    }

    @PostMapping("/subscribers/{id}/photo/delete")
    public String deleteSubscriberPhoto(@PathVariable Long id,
                                        RedirectAttributes redirectAttributes) {
        try {
            subscriberService.deletePhoto(id);
            redirectAttributes.addFlashAttribute("successMessage", "Фотография удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile/" + id;
    }

    @PostMapping("/subscribers/{id}/email")
    public String updateSubscriberEmail(@PathVariable Long id,
                                        @RequestParam("email") String email,
                                        RedirectAttributes redirectAttributes) {
        try {
            EmailUpdateRequest request = new EmailUpdateRequest();
            request.setEmail(email);
            subscriberService.updateEmailJdbcTemplate(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Email успешно сохранён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile/" + id;
    }

    @PostMapping("/subscribers/{id}/tariff")
    public String updateSubscriberTariff(@PathVariable Long id,
                                         @RequestParam("tariffPlan") String tariffPlan,
                                         RedirectAttributes redirectAttributes) {
        try {
            kg.nurtelecom.o_subscriber_service.dto.TariffUpdateRequest request =
                    new kg.nurtelecom.o_subscriber_service.dto.TariffUpdateRequest();
            request.setTariffPlan(kg.nurtelecom.o_subscriber_service.entity.TariffPlan.valueOf(tariffPlan));

            subscriberService.updateTariff(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Тариф успешно обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile/" + id;
    }
}