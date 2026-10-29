package es.us.dp1.chess.tournament.practice.x.transactions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

// Class-level @Transactional: every public method is transactional with the default settings;
// a method-level annotation overrides it for that method.
@Service
@Transactional
public class WalletService {

    private final WalletRepository repo;

    @Autowired
    public WalletService(WalletRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Wallet> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Wallet findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet", "id", id));
    }

    // No method-level annotation: IllegalArgumentException is unchecked, and the class-level
    // @Transactional already rolls back on unchecked exceptions
    public Wallet deposit(Integer id, int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("The amount must be positive");
        Wallet wallet = findById(id);
        wallet.setBalance(wallet.getBalance() + amount);
        return repo.save(wallet);
    }

    // InsufficientFundsException is checked: without rollbackFor the transaction would commit
    @Transactional(rollbackFor = InsufficientFundsException.class)
    public Wallet withdraw(Integer id, int amount) throws InsufficientFundsException {
        if (amount <= 0)
            throw new IllegalArgumentException("The amount must be positive");
        Wallet wallet = findById(id);
        if (wallet.getBalance() < amount)
            throw new InsufficientFundsException();
        wallet.setBalance(wallet.getBalance() - amount);
        return repo.save(wallet);
    }

    // The funds are checked (inside withdraw) before anything is saved
    @Transactional(rollbackFor = InsufficientFundsException.class)
    public void transfer(Integer fromId, Integer toId, int amount) throws InsufficientFundsException {
        withdraw(fromId, amount);
        deposit(toId, amount);
    }

}
