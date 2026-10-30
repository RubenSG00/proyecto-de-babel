package es.us.dp1.chess.tournament.practice.x.logging;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscountCalculator {

    // DEBUG: diagnostic detail; TRACE: the finest level, only shown when TRACE is enabled
    public int discountFor(int amount) {
        log.debug("Computing discount for {}", amount);
        if (amount >= 100) {
            log.trace("Discount rule applied: {}", "10%");
            return amount / 10;
        }
        log.trace("Discount rule applied: {}", "none");
        return 0;
    }

}
