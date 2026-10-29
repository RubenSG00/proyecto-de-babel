package es.us.dp1.chess.tournament.practice.x.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;
    private final UserService userService;

    @Autowired
    public ExpenseService(ExpenseRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    // Security checks are application logic: they belong to the service layer (T4 p.17)
    @Transactional(readOnly = true)
    public List<Expense> findVisibleExpenses() {
        User current = userService.findCurrentUser();
        if (current.hasAuthority("ADMIN"))
            return repo.findAll();
        return repo.findByOwner(current);
    }

    @Transactional(readOnly = true)
    public Expense findExpense(Integer id) {
        Expense expense = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id));
        User current = userService.findCurrentUser();
        if (!current.hasAuthority("ADMIN") && !isOwner(expense, current))
            throw new AccessDeniedException("Only the owner of the expense or an administrator can see it");
        return expense;
    }

    // User does not override equals: compare ids, not instances
    private boolean isOwner(Expense expense, User user) {
        return expense.getOwner().getId().equals(user.getId());
    }

    // An authority rule (only ADMIN) and an ownership rule (not your own expense)
    @Transactional
    public Expense approve(Integer id) {
        Expense expense = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id));
        User current = userService.findCurrentUser();
        if (!current.hasAuthority("ADMIN"))
            throw new AccessDeniedException("Only administrators can approve expenses");
        if (isOwner(expense, current))
            throw new AccessDeniedException("Nobody can approve their own expenses");
        expense.setStatus(ExpenseStatus.APPROVED);
        expense.setApprovedBy(current);
        return repo.save(expense);
    }

}
