package kg.nurtelecom.o_subscriber_service.component;

import org.springframework.stereotype.Component;

@Component
public class PhoneNormalizationComponent {

    public String normalize(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        String digits = raw.replaceAll("\\D", "");

        if (digits.startsWith("0") && digits.length() == 10) {
            digits = "996" + digits.substring(1);
        }

        if (digits.length() == 9) {
            digits = "996" + digits;
        }

        if (digits.length() != 12 || !digits.startsWith("996")) {
            return null;
        }

        return "+" + digits;
    }
}