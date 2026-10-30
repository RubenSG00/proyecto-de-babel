package es.us.dp1.chess.tournament.practice.x.mapping;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DataJpaTest
public class TestX23 {

    @Autowired
    EntityManager em;

    static LoyaltyCard card(String number, String holder) {
        LoyaltyCard c = new LoyaltyCard();
        c.setCardNumber(number);
        c.setHolder(holder);
        return c;
    }

    static TaxInvoice invoice(int year, int serial) {
        TaxInvoice i = new TaxInvoice();
        i.setFiscalYear(year);
        i.setSerialNumber(serial);
        i.setAmount(new BigDecimal("100.00"));
        return i;
    }

    @Test
    public void testX23CardNumberIsUnique() {
        em.persist(card("C-001", "Ana"));
        em.flush();
        em.persist(card("C-002", "Ana"));
        assertDoesNotThrow(() -> em.flush(), "The same holder can have two cards");
        assertThrows(Exception.class, () -> {
            em.persist(card("C-001", "Luis"));
            em.flush();
        }, "Two cards with the same number were stored");
    }

    @Test
    public void testX23TheYearAndTheSerialCanBeRepeatedSeparately() {
        em.persist(invoice(2026, 1));
        em.persist(invoice(2026, 2));
        em.persist(invoice(2027, 1));
        assertDoesNotThrow(() -> em.flush(), "The same year or the same serial number can be repeated separately");
    }

    @Test
    public void testX23ThePairIsUnique() {
        em.persist(invoice(2026, 1));
        em.flush();
        assertThrows(Exception.class, () -> {
            em.persist(invoice(2026, 1));
            em.flush();
        }, "Two invoices with the same (fiscalYear, serialNumber) were stored");
    }

    @Test
    public void testX23Ranges() {
        Validator v = Validation.buildDefaultValidatorFactory().getValidator();
        assertTrue(v.validate(invoice(2000, 0)).isEmpty());
        assertTrue(v.validate(invoice(3000, 5)).isEmpty());
        assertFalse(v.validate(invoice(1999, 1)).isEmpty(), "fiscalYear 1999 must be invalid");
        assertFalse(v.validate(invoice(3001, 1)).isEmpty(), "fiscalYear 3001 must be invalid");
        assertFalse(v.validate(invoice(2026, -1)).isEmpty(), "A negative serialNumber must be invalid");
    }

}
