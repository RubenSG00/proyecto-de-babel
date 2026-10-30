package es.us.dp1.chess.tournament.practice.x.customvalidation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class TestX41 {

    static final LocalDateTime IN = LocalDateTime.of(2026, 11, 3, 14, 0);

    RoomBookingValidator validator = new RoomBookingValidator();

    static RoomBooking booking(LocalDateTime in, LocalDateTime out, Integer guests) {
        RoomBooking b = new RoomBooking();
        b.setRoom("101");
        b.setCheckIn(in);
        b.setCheckOut(out);
        b.setGuests(guests);
        return b;
    }

    private List<String> errors(RoomBooking b) {
        Errors errors = new BeanPropertyBindingResult(b, "booking");
        validator.validate(b, errors);
        return errors.getFieldErrors().stream().map(e -> e.getField() + ":" + e.getCode()).toList();
    }

    @Test
    public void testX41Supports() {
        assertTrue(validator.supports(RoomBooking.class));
        assertTrue(validator.supports(new RoomBooking() { }.getClass()), "Subclasses of RoomBooking are supported too");
        assertFalse(validator.supports(String.class));
    }

    @Test
    public void testX41AValidBookingHasNoErrors() {
        assertEquals(List.of(), errors(booking(IN, IN.plusDays(2), 2)));
        assertEquals(List.of(), errors(booking(IN, IN.plusMinutes(1), 1)));
        assertEquals(List.of(), errors(booking(IN, IN.plusDays(1), 4)));
    }

    @Test
    public void testX41CheckInIsRequired() {
        assertTrue(errors(booking(null, IN, 2)).contains("checkIn:booking.checkIn.required"));
    }

    @Test
    public void testX41CheckOutMustBeAfterCheckIn() {
        assertEquals(List.of("checkOut:booking.checkOut.beforeCheckIn"), errors(booking(IN, IN, 2)));
        assertEquals(List.of("checkOut:booking.checkOut.beforeCheckIn"), errors(booking(IN, IN.minusDays(1), 2)));
        assertEquals(List.of("checkOut:booking.checkOut.beforeCheckIn"), errors(booking(IN, null, 2)));
    }

    @Test
    public void testX41Guests() {
        for (Integer bad : new Integer[] { null, 0, 5 })
            assertEquals(List.of("guests:booking.guests.invalid"), errors(booking(IN, IN.plusDays(1), bad)),
                    bad + " guests must be invalid");
    }

    @Test
    public void testX41SeveralErrorsAtOnce() {
        List<String> all = errors(booking(null, null, 9));
        assertTrue(all.contains("checkIn:booking.checkIn.required"));
        assertTrue(all.contains("guests:booking.guests.invalid"));
    }

}
