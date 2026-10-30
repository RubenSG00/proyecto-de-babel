package es.us.dp1.chess.tournament.practice.x.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    // Domain logic: rules about the membership's own data live in the entity

    public void renew(int months) {
        if (Boolean.TRUE.equals(cancelled))
            throw new IllegalStateException("A cancelled membership cannot be renewed");
        if (months <= 0)
            throw new IllegalArgumentException("The number of months must be positive");
        endDate = endDate.plusMonths(months);
    }

    // Half of the monthly fee for each FULL month remaining (ChronoUnit counts full months)
    public BigDecimal calculateEarlyTerminationFee(LocalDate today) {
        long months = ChronoUnit.MONTHS.between(today, endDate);
        if (months <= 0)
            return BigDecimal.ZERO;
        return monthlyFee.multiply(BigDecimal.valueOf(months)).multiply(new BigDecimal("0.5"));
    }

    public void cancel() {
        cancelled = true;
    }

}
