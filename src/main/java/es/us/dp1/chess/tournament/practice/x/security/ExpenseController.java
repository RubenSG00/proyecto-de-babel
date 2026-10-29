package es.us.dp1.chess.tournament.practice.x.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService service;

    @Autowired
    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> findVisibleExpenses() {
        return new ResponseEntity<>(service.findVisibleExpenses(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Expense> findExpense(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findExpense(id), HttpStatus.OK);
    }

    @PostMapping("{id}/approve")
    public ResponseEntity<Expense> approve(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.approve(id), HttpStatus.OK);
    }

}
