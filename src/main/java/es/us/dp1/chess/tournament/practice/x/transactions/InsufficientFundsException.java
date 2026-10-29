package es.us.dp1.chess.tournament.practice.x.transactions;

public class InsufficientFundsException extends Exception {

    private static final long serialVersionUID = 1L;

    public InsufficientFundsException() {
        super("The balance of the wallet is not enough");
    }

}
