package es.us.dp1.chess.tournament.practice.x.entitymanager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class TestX50 {

    @Autowired
    EntityManager em;

    @Test
    public void testX50EveryMethodIsTransactional() {
        for (String name : new String[] { "create", "find", "raiseSalary", "remove" }) {
            boolean annotated = false;
            for (Method m : EmployeeDao.class.getDeclaredMethods())
                if (m.getName().equals(name))
                    annotated = m.getAnnotation(Transactional.class) != null;
            assertTrue(annotated, name + " must be @Transactional");
        }
    }

    @Test
    public void testX50CreateAndFind() {
        EmployeeDao dao = new EmployeeDao(em);
        Employee e = dao.create("Ana", 2000);
        assertNotNull(e, "create must return the new employee");
        assertNotNull(e.getId(), "persist assigns the generated id");
        em.flush();
        em.clear();
        Employee found = dao.find(e.getId());
        assertNotNull(found);
        assertEquals("Ana", found.getName());
        assertEquals(2000, found.getSalary());
        assertNull(dao.find(474629), "find returns null for an unknown id");
    }

    @Test
    public void testX50RaiseSalaryOnlyUsesFind() {
        Employee e = new EmployeeDao(em).create("Luis", 1500);
        em.flush();
        em.clear();
        // A mock that delegates everything to the real EntityManager but records the calls
        EntityManager spy = mock(EntityManager.class, withSettings().defaultAnswer(AdditionalAnswers.delegatesTo(em)));
        Employee raised = new EmployeeDao(spy).raiseSalary(e.getId(), 300);
        assertEquals(1800, raised.getSalary());
        verify(spy, never()).merge(any());
        verify(spy, never()).persist(any());
        em.flush();
        em.clear();
        assertEquals(1800, em.find(Employee.class, e.getId()).getSalary(),
                "The new salary must reach the database: the employee found is managed");
    }

    @Test
    public void testX50Remove() {
        EmployeeDao dao = new EmployeeDao(em);
        Employee e = dao.create("Eva", 1000);
        em.flush();
        dao.remove(e.getId());
        em.flush();
        em.clear();
        assertNull(em.find(Employee.class, e.getId()));
        assertDoesNotThrow(() -> dao.remove(474629), "Removing an unknown id does nothing");
    }

}
