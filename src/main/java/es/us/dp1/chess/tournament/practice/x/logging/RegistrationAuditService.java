package es.us.dp1.chess.tournament.practice.x.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

// @Slf4j: Lombok creates "private static final Logger log" for this class
@Slf4j
@Service
public class RegistrationAuditService {

    private final RegistrationStore store;

    @Autowired
    public RegistrationAuditService(RegistrationStore store) {
        this.store = store;
    }

    public void register(String username) {
        if (username == null || username.isBlank()) {
            log.warn("Rejected registration: invalid username '{}'", username);
            throw new IllegalArgumentException("Invalid username");
        }
        try {
            store.save(username);
        } catch (RuntimeException e) {
            // The exception as last argument: the stack trace is written with the message
            log.error("Registration failed for {}", username, e);
            throw e;
        }
        log.info("User {} registered", username);
    }

}
