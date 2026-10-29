package es.us.dp1.chess.tournament.practice.x.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;

public class TestX01 extends ReflexiveTest {

    Validator validator;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Speaker validSpeaker() {
        Speaker s = new Speaker();
        s.setFullName("Judit Polgar");
        s.setEmail("judit@example.com");
        s.setPhone("612345678");
        s.setAge(48);
        s.setRating(new BigDecimal("4.5"));
        s.setBirthDate(LocalDate.of(1976, 7, 23));
        s.setBio("Strongest female chess player of all time.");
        return s;
    }

    private void assertValid(Speaker s, String what) {
        assertTrue(validator.validate(s).isEmpty(), what + " should be valid, but it was rejected");
    }

    private void assertInvalid(Speaker s, String what) {
        assertFalse(validator.validate(s).isEmpty(), what + " should be rejected, but it was considered valid");
    }

    @Test
    public void testX01ValidSpeakerIsValid() {
        assertValid(validSpeaker(), "A speaker with correct values");
    }

    @Test
    public void testX01FullName() throws NoSuchFieldException {
        for (String v : new String[] { null, "Al", "    ", "x".repeat(41) }) {
            Speaker s = validSpeaker();
            s.setFullName(v);
            assertInvalid(s, "fullName '" + v + "'");
        }
        Speaker s = validSpeaker();
        s.setFullName("Bob");
        assertValid(s, "fullName 'Bob' (3 characters)");
        s.setFullName("x".repeat(40));
        assertValid(s, "fullName of 40 characters");
        assertTrue(Speaker.class.getDeclaredField("fullName").isAnnotationPresent(Size.class),
                "The length of fullName must be constrained with the Jakarta annotation @Size");
    }

    @Test
    public void testX01Email() {
        for (String v : new String[] { null, "not-an-email", "judit@" }) {
            Speaker s = validSpeaker();
            s.setEmail(v);
            assertInvalid(s, "email '" + v + "'");
        }
    }

    @Test
    public void testX01Phone() {
        for (String v : new String[] { null, "12345678", "1234567890", "61234567a", "612 345 67" }) {
            Speaker s = validSpeaker();
            s.setPhone(v);
            assertInvalid(s, "phone '" + v + "'");
        }
        Speaker s = validSpeaker();
        s.setPhone("000000000");
        assertValid(s, "phone '000000000'");
    }

    @Test
    public void testX01Age() {
        for (Integer v : new Integer[] { null, 17, 100 }) {
            Speaker s = validSpeaker();
            s.setAge(v);
            assertInvalid(s, "age " + v);
        }
        for (Integer v : new Integer[] { 18, 99 }) {
            Speaker s = validSpeaker();
            s.setAge(v);
            assertValid(s, "age " + v);
        }
    }

    @Test
    public void testX01Rating() {
        for (String v : new String[] { "-0.1", "5.1", "4.55", "10.0" }) {
            Speaker s = validSpeaker();
            s.setRating(new BigDecimal(v));
            assertInvalid(s, "rating " + v);
        }
        for (String v : new String[] { "0.0", "5.0", "3.7" }) {
            Speaker s = validSpeaker();
            s.setRating(new BigDecimal(v));
            assertValid(s, "rating " + v);
        }
        Speaker s = validSpeaker();
        s.setRating(null);
        assertValid(s, "A speaker without rating");
    }

    @Test
    public void testX01BirthDate() {
        Speaker s = validSpeaker();
        s.setBirthDate(LocalDate.now().plusDays(1));
        assertInvalid(s, "A birth date in the future");
        s.setBirthDate(null);
        assertValid(s, "A speaker without birth date");
    }

    @Test
    public void testX01Bio() {
        Speaker s = validSpeaker();
        s.setBio("x".repeat(301));
        assertInvalid(s, "A bio of 301 characters");
        s.setBio("x".repeat(300));
        assertValid(s, "A bio of 300 characters");
        s.setBio(null);
        assertValid(s, "A speaker without bio");
    }

}
