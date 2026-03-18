package kg.nurtelecom.o_subscriber_service.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.o_subscriber_service.dto.RegisterRequest;
import kg.nurtelecom.o_subscriber_service.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            registrationService.register(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Регистрация успешно завершена. Теперь вы можете войти в систему.");
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("registerError", e.getMessage());
            return "register";
        }
    }
}