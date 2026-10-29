package es.us.dp1.chess.tournament.practice.x.logging;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

// Given: stores the registered usernames (it may fail with a RuntimeException)
@Component
public class RegistrationStore {

    private final List<String> usernames = new ArrayList<>();

    public void save(String username) {
        usernames.add(username);
    }

    public List<String> getUsernames() {
        return usernames;
    }

}
