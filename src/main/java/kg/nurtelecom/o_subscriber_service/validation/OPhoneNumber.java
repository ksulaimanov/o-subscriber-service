package kg.nurtelecom.o_subscriber_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OPhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OPhoneNumber {

    String message() default "Номер должен принадлежать оператору O! и быть корректным номером КР";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}