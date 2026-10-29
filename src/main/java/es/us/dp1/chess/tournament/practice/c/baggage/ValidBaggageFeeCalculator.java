package es.us.dp1.chess.tournament.practice.c.baggage;

public class ValidBaggageFeeCalculator implements BaggageFeeCalculator {

    @Override
    public int calculateFee(int[] bagWeightsKg, boolean businessClass) throws InvalidBagException {
        if (bagWeightsKg == null || bagWeightsKg.length == 0) {
            return 0;
        }
        for (int weight : bagWeightsKg) {
            if (weight <= 0 || weight > 50) {
                throw new InvalidBagException();
            }
        }
        int freeBags = businessClass ? 2 : 1;
        int weightLimit = businessClass ? 32 : 23;
        int fee = Math.max(0, bagWeightsKg.length - freeBags) * 50;
        for (int weight : bagWeightsKg) {
            if (weight > weightLimit) {
                fee += 75;
            }
        }
        return fee;
    }

}
