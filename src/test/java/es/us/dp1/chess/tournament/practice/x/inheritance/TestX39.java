package es.us.dp1.chess.tournament.practice.x.inheritance;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DataJpaTest
public class TestX39 extends ReflexiveTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager em;

    Validator v = Validation.buildDefaultValidatorFactory().getValidator();

    private boolean hasTable(String table) {
        Integer n = new JdbcTemplate(dataSource).queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = ?", Integer.class, table.toUpperCase());
        return n != null && n > 0;
    }

    private Object vehicle(Class<?> type, String plate, String brand) throws Exception {
        Object o = type.getDeclaredConstructor().newInstance();
        Setters.set(o, "plate", plate);
        Setters.set(o, "brand", brand);
        return o;
    }

    @Test
    public void testX39VehicleIsAnAbstractMappedSuperclass() {
        assertNotNull(Vehicle.class.getAnnotation(MappedSuperclass.class), "Vehicle must be a @MappedSuperclass");
        assertTrue(Modifier.isAbstract(Vehicle.class.getModifiers()), "Vehicle must be abstract");
        assertEquals(BaseEntity.class, Vehicle.class.getSuperclass(), "Vehicle must extend BaseEntity");
        assertEquals(Vehicle.class, Car.class.getSuperclass(), "Car must extend Vehicle");
        assertEquals(Vehicle.class, Motorbike.class.getSuperclass(), "Motorbike must extend Vehicle");
        assertFalse(hasTable("vehicle"), "There must be no table for vehicles");
    }

    @Test
    public void testX39CarConstraintsAndPersistence() throws Exception {
        Object car = vehicle(Car.class, "1234BCD", "Seat");
        Setters.set(car, "doors", 5);
        assertTrue(v.validate(car).isEmpty());
        em.persist(car);
        em.flush();
        for (String[] bad : new String[][] { { "123BCD", "Seat" }, { "1234bcd", "Seat" }, { "1234BCD", " " } }) {
            Object c = vehicle(Car.class, bad[0], bad[1]);
            Setters.set(c, "doors", 3);
            assertFalse(v.validate(c).isEmpty(), "plate '" + bad[0] + "' / brand '" + bad[1] + "' must be invalid");
        }
        Setters.set(car, "doors", 6);
        assertFalse(v.validate(car).isEmpty(), "A car has at most 5 doors");
        Setters.set(car, "doors", 1);
        assertFalse(v.validate(car).isEmpty(), "A car has at least 2 doors");
    }

    @Test
    public void testX39MotorbikeConstraints() throws Exception {
        Object m = vehicle(Motorbike.class, "9876XYZ", "Honda");
        Setters.set(m, "engineCc", 125);
        assertTrue(v.validate(m).isEmpty());
        Setters.set(m, "engineCc", 49);
        assertFalse(v.validate(m).isEmpty(), "A motorbike has at least 50 cc");
        Setters.set(m, "engineCc", 125);
        Setters.set(m, "plate", null);
        assertFalse(v.validate(m).isEmpty(), "The plate is mandatory");
    }

}
