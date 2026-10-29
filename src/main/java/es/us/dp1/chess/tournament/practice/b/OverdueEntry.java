package es.us.dp1.chess.tournament.practice.b;

import es.us.dp1.chess.tournament.user.User;

public class OverdueEntry {

    private User user;
    private Long overdueLoans;

    public OverdueEntry(User user, Long overdueLoans) {
        this.user = user;
        this.overdueLoans = overdueLoans;
    }

    public User getUser() {
        return user;
    }

    public Long getOverdueLoans() {
        return overdueLoans;
    }

}
