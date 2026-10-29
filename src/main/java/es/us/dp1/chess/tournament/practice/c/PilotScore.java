package es.us.dp1.chess.tournament.practice.c;

import es.us.dp1.chess.tournament.user.User;

public class PilotScore {

    private User pilot;
    private Long score;

    public PilotScore(User pilot, Long score) {
        this.pilot = pilot;
        this.score = score;
    }

    public User getPilot() {
        return pilot;
    }

    public Long getScore() {
        return score;
    }

}
