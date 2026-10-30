package es.us.dp1.chess.tournament.practice.x.mapping;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@DataJpaTest
public class TestX22 {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager em;

    private Map<String, Object> column(String table, String column) {
        List<Map<String, Object>> rows = new JdbcTemplate(dataSource).queryForList(
                "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE UPPER(TABLE_NAME) = ? AND UPPER(COLUMN_NAME) = ?",
                table.toUpperCase(), column.toUpperCase());
        return rows.isEmpty() ? null : rows.get(0);
    }

    @Test
    public void testX22TableAndColumnNames() {
        assertNotNull(column("clinic_visits", "visit_date"), "The table clinic_visits must have the column visit_date");
        assertNotNull(column("clinic_visits", "notes"), "The table clinic_visits must have the column notes");
        assertNotNull(column("clinic_visits", "pet_birth_date"), "The table clinic_visits must have the column pet_birth_date");
    }

    @Test
    public void testX22PetBirthDateIsADate() throws NoSuchFieldException {
        Temporal t = ClinicVisit.class.getDeclaredField("petBirthDate").getAnnotation(Temporal.class);
        assertNotNull(t, "petBirthDate must be annotated with @Temporal");
        assertEquals(TemporalType.DATE, t.value());
        assertEquals("DATE", String.valueOf(column("clinic_visits", "pet_birth_date").get("DATA_TYPE")));
    }

    @Test
    public void testX22JpqlStillUsesTheEntityName() {
        ClinicVisit v = new ClinicVisit();
        v.setDate(LocalDateTime.of(2026, 10, 1, 9, 30));
        v.setDescription("Vaccination");
        v.setPetBirthDate(new Date());
        em.persist(v);
        em.flush();
        assertEquals(1L, em.createQuery("SELECT COUNT(v) FROM ClinicVisit v WHERE v.description = 'Vaccination'").getSingleResult());
    }

}
