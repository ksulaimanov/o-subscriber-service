package kg.nurtelecom.o_subscriber_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/error/403")
    public String accessDeniedPage() {
        return "error/403";
    }

    @GetMapping("/error/404")
    public String notFoundPage() {
        return "error/404";
    }

    @GetMapping("/error/500")
    public String internalErrorPage() {
        return "error/500";
    }
}