package es.us.dp1.chess.tournament.practice.x.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Replace System.out with an SLF4J logger to solve Test X48
@Service
public class RegistrationAuditService {

    private final RegistrationStore store;

    @Autowired
    public RegistrationAuditService(RegistrationStore store) {
        this.store = store;
    }

    public void register(String username) {
        if (username == null || username.isBlank()) {
            System.out.println("Rejected registration: invalid username '" + username + "'");
            throw new IllegalArgumentException("Invalid username");
        }
        try {
            store.save(username);
        } catch (RuntimeException e) {
            System.out.println("Registration failed for " + username);
            e.printStackTrace();
            throw e;
        }
        System.out.println("User " + username + " registered");
    }

}
