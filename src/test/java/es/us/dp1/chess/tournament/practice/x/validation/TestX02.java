package es.us.dp1.chess.tournament.practice.x.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class TestX02 {

    Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private EventRegistrationRequest validRequest() {
        EventRegistrationRequest r = new EventRegistrationRequest();
        r.setNickname("magnus");
        r.setDisplayName("Magnus C.");
        r.setInterests(new ArrayList<>(List.of("openings")));
        r.setLanguages(new ArrayList<>(List.of("en")));
        r.setSeats(2);
        set(r, "maxPrice", 50.0);
        return r;
    }

    private void check(String field, Object value, boolean valid) {
        EventRegistrationRequest r = validRequest();
        set(r, field, value);
        boolean isValid = validator.validate(r).isEmpty();
        assertEquals(valid, isValid, field + " = '" + value + "' should be " + (valid ? "valid" : "invalid"));
    }

    // Sets the attribute directly, so that the test works whatever the declared type (double or Double)
    private static void set(Object o, String fieldName, Object value) {
        try {
            Field f = o.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(o, value);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            fail("The attribute " + fieldName + " could not be set to " + value + ": " + e);
        }
    }

    @Test
    public void testX02ValidRequestIsValid() {
        assertTrue(validator.validate(validRequest()).isEmpty(), "A request with correct values was rejected");
    }

    @Test
    public void testX02Nickname() {
        check("nickname", null, false);
        check("nickname", "", true);
        check("nickname", "   ", true);
    }

    @Test
    public void testX02DisplayName() {
        check("displayName", null, false);
        check("displayName", "", false);
        check("displayName", "   ", false);
        check("displayName", " x ", true);
    }

    @Test
    public void testX02Interests() {
        check("interests", null, false);
        check("interests", new ArrayList<String>(), true);
    }

    @Test
    public void testX02Languages() {
        check("languages", null, false);
        check("languages", new ArrayList<String>(), false);
        check("languages", new ArrayList<>(List.of("es", "en")), true);
    }

    @Test
    public void testX02Seats() {
        check("seats", null, false);
        check("seats", 0, false);
        check("seats", 11, false);
        check("seats", 1, true);
        check("seats", 10, true);
    }

    @Test
    public void testX02MaxPriceIsADoubleWithDecimalBounds() throws NoSuchFieldException {
        assertEquals(Double.class, EventRegistrationRequest.class.getDeclaredField("maxPrice").getType(),
                "maxPrice must be declared as Double (a double can never be null)");
        check("maxPrice", null, false);
        check("maxPrice", 0.4, false);
        check("maxPrice", 100.0, false);
        check("maxPrice", 0.5, true);
        check("maxPrice", 99.9, true);
    }

}
