package es.us.dp1.chess.tournament.practice.x.entitymanager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@Repository
public class EmployeeDao {

    private final EntityManager em;

    public EmployeeDao(EntityManager em) {
        this.em = em;
    }

    // Test X50

    // new -> managed: persist assigns the id now; the INSERT waits for the flush
    @Transactional
    public Employee create(String name, long salary) {
        Employee e = new Employee();
        e.setName(name);
        e.setSalary(salary);
        em.persist(e);
        return e;
    }

    @Transactional(readOnly = true)
    public Employee find(Integer id) {
        return em.find(Employee.class, id);
    }

    // The employee returned by find is managed: the change is written at the flush (dirty checking),
    // so no persist/merge is needed
    @Transactional
    public Employee raiseSalary(Integer id, long raise) {
        Employee e = em.find(Employee.class, id);
        e.setSalary(e.getSalary() + raise);
        return e;
    }

    @Transactional
    public void remove(Integer id) {
        Employee e = em.find(Employee.class, id);
        if (e != null)
            em.remove(e);
    }

    // TODO: Test X51

    public Employee detachAndChangeSalary(Integer id, long newSalary) {
        return null;
    }

    public Employee mergeChanges(Employee detached) {
        return null;
    }

    public void removeAndRestore(Integer id) {
    }

}
