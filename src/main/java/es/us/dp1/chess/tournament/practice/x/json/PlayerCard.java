package es.us.dp1.chess.tournament.practice.x.json;

// DTO: only the public data of a user, so the password hash (and the id) never leave the API
public class PlayerCard {

    private final String username;
    private final Integer rating;
    private final String authority;

    public PlayerCard(String username, Integer rating, String authority) {
        this.username = username;
        this.rating = rating;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public Integer getRating() {
        return rating;
    }

    public String getAuthority() {
        return authority;
    }

}
