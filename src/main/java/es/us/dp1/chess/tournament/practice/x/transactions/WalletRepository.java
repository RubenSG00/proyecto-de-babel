package es.us.dp1.chess.tournament.practice.x.transactions;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {
    List<Wallet> findAll();
}
