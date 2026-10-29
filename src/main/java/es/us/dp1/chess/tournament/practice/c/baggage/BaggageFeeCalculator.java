package es.us.dp1.chess.tournament.practice.c.baggage;

public interface BaggageFeeCalculator {

    /**
     * Fee, in euros, for checking in bags with the given weights. A null or empty array costs 0.
     * Economy passengers check in 1 bag for free and business passengers 2; every extra bag costs
     * 50. Every bag heavier than the limit (strictly more than 23 kg in economy, 32 kg in business)
     * costs 75 more, whether it is free or not.
     *
     * @throws InvalidBagException if any bag weighs 0 kg or less, or more than 50 kg.
     */
    int calculateFee(int[] bagWeightsKg, boolean businessClass) throws InvalidBagException;

}
