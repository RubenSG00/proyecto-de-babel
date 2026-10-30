package es.us.dp1.chess.tournament.practice.x.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

@Service
public class GymMembershipService {

    private final GymMembershipRepository repo;

    @Autowired
    public GymMembershipService(GymMembershipRepository repo) {
        this.repo = repo;
    }

    // Application logic: transactions, finding and saving, coordinating the domain operations

    @Transactional
    public GymMembership renew(Integer id, int months) {
        GymMembership membership = findById(id);
        membership.renew(months);
        return repo.save(membership);
    }

    @Transactional
    public BigDecimal cancel(Integer id, LocalDate today) {
        GymMembership membership = findById(id);
        BigDecimal fee = membership.calculateEarlyTerminationFee(today);
        membership.cancel();
        repo.save(membership);
        return fee;
    }

    private GymMembership findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("GymMembership", "id", id));
    }

}
