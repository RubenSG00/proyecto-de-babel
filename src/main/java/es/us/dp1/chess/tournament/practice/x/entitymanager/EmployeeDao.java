package es.us.dp1.chess.tournament.practice.x.entitymanager;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class EmployeeDao {

    private final EntityManager em;

    public EmployeeDao(EntityManager em) {
        this.em = em;
    }

    // TODO: Test X50

    public Employee create(String name, long salary) {
        return null;
    }

    public Employee find(Integer id) {
        return null;
    }

    public Employee raiseSalary(Integer id, long raise) {
        return null;
    }

    public void remove(Integer id) {
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
