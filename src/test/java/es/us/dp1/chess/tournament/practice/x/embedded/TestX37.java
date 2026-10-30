package es.us.dp1.chess.tournament.practice.x.embedded;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityManager;

@DataJpaTest
public class TestX37 {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager em;

    static Contact contact(String name, String phone) {
        Contact c = new Contact();
        c.setName(name);
        c.setPhone(phone);
        return c;
    }

    @Test
    public void testX37ColumnsOfBothContacts() {
        assertNotNull(Contact.class.getAnnotation(Embeddable.class), "Contact must be @Embeddable");
        for (String column : new String[] { "sales_name", "sales_phone", "support_name", "support_phone" })
            assertTrue(SchemaHelper.hasColumn(dataSource, "supplier", column),
                    "The table supplier must have the column " + column);
    }

    @Test
    public void testX37BothContactsAreStoredSeparately() {
        Supplier s = new Supplier();
        s.setName("Chess Supplies S.L.");
        s.setSalesContact(contact("Marta", "600111222"));
        s.setSupportContact(contact("Pedro", "600333444"));
        em.persist(s);
        em.flush();
        em.clear();
        Supplier stored = em.find(Supplier.class, s.getId());
        assertEquals("Marta", stored.getSalesContact().getName());
        assertEquals("600333444", stored.getSupportContact().getPhone());
    }

}
