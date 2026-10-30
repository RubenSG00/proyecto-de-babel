package es.us.dp1.chess.tournament.practice.x.entitymanager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class TestX51 {

    @Autowired
    EntityManager em;

    private Employee employee(String name, long salary) {
        Employee e = new Employee();
        e.setName(name);
        e.setSalary(salary);
        em.persist(e);
        em.flush();
        return e;
    }

    @Test
    public void testX51ChangesOfADetachedEmployeeAreLost() {
        Employee e = employee("Ana", 2000);
        Employee detached = new EmployeeDao(em).detachAndChangeSalary(e.getId(), 9999);
        assertNotNull(detached);
        assertEquals(9999, detached.getSalary());
        assertFalse(em.contains(detached), "The employee must be detached");
        em.flush();
        em.clear();
        assertEquals(2000, em.find(Employee.class, e.getId()).getSalary(), "The change must not reach the database");
    }

    @Test
    public void testX51MergeReturnsAManagedCopy() {
        Employee e = employee("Luis", 1500);
        em.detach(e);
        e.setSalary(1700);
        Employee managed = new EmployeeDao(em).mergeChanges(e);
        assertNotNull(managed);
        assertTrue(em.contains(managed), "merge returns the managed instance");
        assertNotSame(e, managed, "merge returns a different object; the argument stays detached");
        em.flush();
        em.clear();
        assertEquals(1700, em.find(Employee.class, e.getId()).getSalary());
    }

    @Test
    public void testX51RemoveCanBeUndone() {
        Employee e = employee("Eva", 1000);
        new EmployeeDao(em).removeAndRestore(e.getId());
        em.flush();
        em.clear();
        assertNotNull(em.find(Employee.class, e.getId()), "The employee must still be in the database");
    }

}
