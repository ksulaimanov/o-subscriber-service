package kg.nurtelecom.o_subscriber_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OPhoneNumberValidator implements ConstraintValidator<OPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String digits = value.replaceAll("\\D", "");

        if (digits.startsWith("0") && digits.length() == 10) {
            digits = "996" + digits.substring(1);
        }

        if (digits.length() == 9) {
            digits = "996" + digits;
        }

        if (digits.length() != 12 || !digits.startsWith("996")) {
            return false;
        }

        int operatorCode;
        try {
            operatorCode = Integer.parseInt(digits.substring(3, 6));
        } catch (NumberFormatException e) {
            return false;
        }

        return isNurTelecomCode(operatorCode);
    }

    private boolean isNurTelecomCode(int code) {
        return (code >= 500 && code <= 509) ||
                (code >= 700 && code <= 709);
    }
}