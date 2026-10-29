package es.us.dp1.chess.tournament.practice.x.security;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.us.dp1.chess.tournament.user.User;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {
    List<Expense> findAll();
    List<Expense> findByOwner(User owner);
}
