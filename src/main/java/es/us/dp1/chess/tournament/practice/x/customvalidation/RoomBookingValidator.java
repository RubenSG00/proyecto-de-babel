package es.us.dp1.chess.tournament.practice.x.customvalidation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// A Spring Validator can check rules that involve several attributes (checkOut after checkIn),
// which an annotation on a single attribute cannot
public class RoomBookingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RoomBooking.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoomBooking booking = (RoomBooking) target;
        if (booking.getCheckIn() == null)
            errors.rejectValue("checkIn", "booking.checkIn.required");
        if (booking.getCheckOut() == null
                || (booking.getCheckIn() != null && !booking.getCheckOut().isAfter(booking.getCheckIn())))
            errors.rejectValue("checkOut", "booking.checkOut.beforeCheckIn");
        Integer guests = booking.getGuests();
        if (guests == null || guests < 1 || guests > 4)
            errors.rejectValue("guests", "booking.guests.invalid");
    }

}
