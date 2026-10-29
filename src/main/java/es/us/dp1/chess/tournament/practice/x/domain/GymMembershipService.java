package es.us.dp1.chess.tournament.practice.x.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GymMembershipService {

    private final GymMembershipRepository repo;

    @Autowired
    public GymMembershipService(GymMembershipRepository repo) {
        this.repo = repo;
    }

    // TODO: Implement the application logic to solve Test X52

    public GymMembership renew(Integer id, int months) {
        return null;
    }

    public BigDecimal cancel(Integer id, LocalDate today) {
        return null;
    }

}
