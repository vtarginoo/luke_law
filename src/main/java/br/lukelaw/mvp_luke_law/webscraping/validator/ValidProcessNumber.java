package br.lukelaw.mvp_luke_law.webscraping.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProcessNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProcessNumber {
    String message() default "O número do processo deve estar em um dos formatos permitidos:" +
            " 'xxxxxxx-xx.2023.8.19.0001' ou 'xxxxxxxxxxxxxxxxxxxx'.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}