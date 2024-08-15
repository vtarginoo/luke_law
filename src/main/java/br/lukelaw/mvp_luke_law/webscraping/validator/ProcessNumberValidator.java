package br.lukelaw.mvp_luke_law.webscraping.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProcessNumberValidator implements ConstraintValidator<ValidProcessNumber, String> {

    public static final String FORMAT_WITH_DASHES = "^\\d{7}-\\d{2}\\.\\d{4}\\.\\d{1}\\.\\d{2}\\.\\d{4}$";
    public static final String FORMAT_WITHOUT_DASHES = "^\\d{20}$";

    @Override
    public void initialize(ValidProcessNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // Se estiver vazio, a validação de @NotBlank falhará antes de chegar aqui
        }
        return value.matches(FORMAT_WITH_DASHES) || value.matches(FORMAT_WITHOUT_DASHES);
    }
}
