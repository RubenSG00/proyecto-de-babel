package es.us.dp1.chess.tournament.practice.x.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GymMembership extends BaseEntity {

    @NotNull
    LocalDate endDate;

    @NotNull
    BigDecimal monthlyFee;

    Boolean cancelled = false;

    // TODO: Implement the domain logic to solve Test X52

    public void renew(int months) {
    }

    public BigDecimal calculateEarlyTerminationFee(LocalDate today) {
        return null;
    }

    public void cancel() {
    }

}
