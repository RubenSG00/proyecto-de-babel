package es.us.dp1.chess.tournament.practice.x.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultipleOfValidator implements ConstraintValidator<MultipleOf, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // TODO: Implement to solve Test X40
        return true;
    }

}
