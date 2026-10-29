package es.us.dp1.chess.tournament.practice.x.logging;

import org.springframework.stereotype.Component;

@Component
public class DiscountCalculator {

    // TODO: Add the DEBUG and TRACE messages to solve Test X49
    public int discountFor(int amount) {
        if (amount >= 100)
            return amount / 10;
        return 0;
    }

}
