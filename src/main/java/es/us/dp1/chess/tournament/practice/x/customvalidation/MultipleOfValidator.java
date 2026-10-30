package es.us.dp1.chess.tournament.practice.x.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultipleOfValidator implements ConstraintValidator<MultipleOf, Integer> {

    private int divisor;

    @Override
    public void initialize(MultipleOf annotation) {
        divisor = annotation.value();
    }

    // null is valid: deciding about nulls is the job of @NotNull
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || value % divisor == 0;
    }

}
