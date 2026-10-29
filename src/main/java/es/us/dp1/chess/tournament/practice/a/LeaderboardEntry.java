package es.us.dp1.chess.tournament.practice.a;

import es.us.dp1.chess.tournament.user.User;

public class LeaderboardEntry {

    private User user;
    private Long points;

    public LeaderboardEntry(User user, Long points) {
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
