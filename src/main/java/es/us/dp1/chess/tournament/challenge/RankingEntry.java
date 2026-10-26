package es.us.dp1.chess.tournament.challenge;

import es.us.dp1.chess.tournament.user.User;

public class RankingEntry {

    private User user;
    private Long points;

    public RankingEntry(User user, Long points) {
        this.user = user;
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public Long getPoints() {
        return points;
    }

}
