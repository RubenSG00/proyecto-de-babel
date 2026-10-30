package es.us.dp1.chess.tournament.practice.x.embedded;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityManager;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DataJpaTest
public class TestX36 {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager em;

    static PetClinic clinic(String street, String city, String zip) {
        PostalAddress a = new PostalAddress();
        a.setStreet(street);
        a.setCity(city);
        a.setZipCode(zip);
        PetClinic c = new PetClinic();
        c.setName("Clinica Triana");
        c.setAddress(a);
        return c;
    }

    @Test
    public void testX36TheAddressIsStoredInTheTableOfTheClinic() {
        assertNotNull(PostalAddress.class.getAnnotation(Embeddable.class), "PostalAddress must be @Embeddable");
        for (String column : new String[] { "street", "city", "zip_code" })
            assertTrue(SchemaHelper.hasColumn(dataSource, "pet_clinic", column),
                    "The table pet_clinic must have the column " + column);
        assertFalse(SchemaHelper.hasTable(dataSource, "postal_address"), "No table may be created for addresses");
    }

    @Test
    public void testX36TheAddressIsSavedAndLoaded() {
        PetClinic c = clinic("Calle Betis 10", "Sevilla", "41010");
        em.persist(c);
        em.flush();
        em.clear();
        PetClinic stored = em.find(PetClinic.class, c.getId());
        assertNotNull(stored.getAddress(), "The address was not stored");
        assertEquals("41010", stored.getAddress().getZipCode());
    }

    @Test
    public void testX36TheConstraintsOfTheAddressAreValidated() {
        Validator v = Validation.buildDefaultValidatorFactory().getValidator();
        assertTrue(v.validate(clinic("Calle Betis 10", "Sevilla", "41010")).isEmpty());
        assertFalse(v.validate(clinic(" ", "Sevilla", "41010")).isEmpty(), "A blank street must be invalid");
        assertFalse(v.validate(clinic("Calle Betis 10", "", "41010")).isEmpty(), "A blank city must be invalid");
        assertFalse(v.validate(clinic("Calle Betis 10", "Sevilla", "4101")).isEmpty(), "A zip code of 4 digits must be invalid");
        assertFalse(v.validate(clinic("Calle Betis 10", "Sevilla", "4101A")).isEmpty(), "A zip code with letters must be invalid");
        PetClinic noAddress = clinic("a", "b", "41010");
        noAddress.setAddress(null);
        assertFalse(v.validate(noAddress).isEmpty(), "The address is mandatory");
    }

}
