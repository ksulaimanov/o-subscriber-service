package kg.nurtelecom.o_subscriber_service.controller;

import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.service.SubscriberService;
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

    public SubscriberViewController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/subscribers-page")
    public String subscribersPage(Model model) {
        List<SubscriberResponse> subscribers = subscriberService.getAllSubscribers();
        model.addAttribute("subscribers", subscribers);
        return "subscribers-page";
    }

    @GetMapping("/profile")
    public String profileRedirect() {
        return "redirect:/subscribers-page";
    }

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable Long id, Model model) {
        SubscriberResponse subscriber = subscriberService.getSubscriberById(id);
        model.addAttribute("subscriber", subscriber);
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
}