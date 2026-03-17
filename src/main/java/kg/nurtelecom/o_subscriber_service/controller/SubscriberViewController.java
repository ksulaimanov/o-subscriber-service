package kg.nurtelecom.o_subscriber_service.controller;

import kg.nurtelecom.o_subscriber_service.dto.SubscriberResponse;
import kg.nurtelecom.o_subscriber_service.service.SubscriberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String profilePage(Model model) {
        List<SubscriberResponse> subscribers = subscriberService.getAllSubscribers();

        if (!subscribers.isEmpty()) {
            model.addAttribute("subscriber", subscribers.get(0));
        }

        return "profile";
    }
}