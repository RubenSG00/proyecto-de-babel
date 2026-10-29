package es.us.dp1.chess.tournament.practice.x.customvalidation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RoomBookingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO: Implement to solve Test X41
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO: Implement to solve Test X41
    }

}
