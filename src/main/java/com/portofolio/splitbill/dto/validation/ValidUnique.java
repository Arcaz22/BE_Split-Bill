package com.portofolio.splitbill.dto.validation;

import java.lang.annotation.*;

import com.portofolio.splitbill.dto.validation.validator.UniqueValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueValidator.class)
public @interface ValidUnique {
    String message() default "Field already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String column();
}
