package es.us.dp1.chess.tournament.practice.x.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Expense> findVisibleExpenses() {
        // TODO: Implement to solve Test X17
        return null;
    }

    public Expense findExpense(Integer id) {
        // TODO: Implement to solve Test X17
        return null;
    }

    public Expense approve(Integer id) {
        // TODO: Implement to solve Test X18
        return null;
    }

}
