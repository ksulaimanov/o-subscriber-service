package kg.nurtelecom.o_subscriber_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OPhoneNumberValidator implements ConstraintValidator<OPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        return value.matches("^\\+996\\d{9}$");
    }
}