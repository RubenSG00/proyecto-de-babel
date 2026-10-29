package es.us.dp1.chess.tournament.practice.x.transactions;

import java.util.List;

public class WalletService {

    private final WalletRepository repo;

    public WalletService(WalletRepository repo) {
        this.repo = repo;
    }

    public List<Wallet> findAll() {
        // TODO: Implement to solve Test X07
        return null;
    }

    public Wallet findById(Integer id) {
        // TODO: Implement to solve Test X07
        return null;
    }

    public Wallet deposit(Integer id, int amount) {
        // TODO: Implement to solve Test X07
        return null;
    }

    public Wallet withdraw(Integer id, int amount) throws InsufficientFundsException {
        // TODO: Implement to solve Test X07
        return null;
    }

    public void transfer(Integer fromId, Integer toId, int amount) throws InsufficientFundsException {
        // TODO: Implement to solve Test X07
    }

}
