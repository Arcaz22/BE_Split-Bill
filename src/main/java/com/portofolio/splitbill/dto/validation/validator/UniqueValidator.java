package com.portofolio.splitbill.dto.validation.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.portofolio.splitbill.dto.validation.ValidUnique;
import com.portofolio.splitbill.model.User;
import com.portofolio.splitbill.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueValidator implements ConstraintValidator<ValidUnique, String> {
    private String column;
    private final UserRepository userRepository;

    public UniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(ValidUnique constraintAnnotation) {
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = Optional.empty();
        switch (column) {
            case "username":
                user = userRepository.findByUsername(value);
                break;
            case "phone":
                user = userRepository.findByPhone(value);
                break;
            default:
                break;
        }

        return user.isEmpty();
    }
}
