package es.us.dp1.chess.tournament.practice.x.relationships;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OneToMany;
import jakarta.validation.Validation;

@DataJpaTest
public class TestX19 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Autowired
    InvoiceLineRepository lines;

    static InvoiceLine line(String product, int units, String price) {
        InvoiceLine l = new InvoiceLine();
        l.setProduct(product);
        l.setUnits(units);
        l.setUnitPrice(new BigDecimal(price));
        return l;
    }

    PurchaseInvoice invoice() {
        PurchaseInvoice i = new PurchaseInvoice();
        i.setCustomer("Club Ajedrez Triana");
        i.setLines(new ArrayList<>(List.of(line("Board", 2, "15.50"), line("Clock", 1, "40.00"))));
        return i;
    }

    @Test
    public void testX19LinesAreAOneToManyComposition() throws NoSuchFieldException {
        checkThatFieldIsAnnotatedWith(PurchaseInvoice.class, "lines", OneToMany.class);
        OneToMany a = PurchaseInvoice.class.getDeclaredField("lines").getAnnotation(OneToMany.class);
        assertTrue(a.orphanRemoval(), "A composition must remove the orphan lines");
    }

    @Test
    public void testX19SavingAndDeletingCascade() {
        PurchaseInvoice i = invoice();
        em.persist(i);
        em.flush();
        assertEquals(2, lines.count(), "Persisting the invoice must persist its lines");
        em.remove(i);
        em.flush();
        assertEquals(0, lines.count(), "Removing the invoice must remove its lines");
    }

    @Test
    public void testX19OrphanLinesAreDeleted() {
        PurchaseInvoice i = invoice();
        em.persist(i);
        em.flush();
        i.getLines().remove(0);
        em.flush();
        assertEquals(1, lines.count(), "A line removed from the collection must be deleted");
    }

    @Test
    public void testX19AtLeastOneLine() {
        PurchaseInvoice i = invoice();
        i.setLines(new ArrayList<>());
        assertFalse(Validation.buildDefaultValidatorFactory().getValidator().validate(i).isEmpty(),
                "An invoice without lines must be invalid (1..n)");
        i.setLines(null);
        assertFalse(Validation.buildDefaultValidatorFactory().getValidator().validate(i).isEmpty(),
                "An invoice with null lines must be invalid (1..n)");
    }

    @Test
    public void testX19Total() {
        assertEquals(0, new BigDecimal("71.00").compareTo(invoice().getTotal()));
    }

}
