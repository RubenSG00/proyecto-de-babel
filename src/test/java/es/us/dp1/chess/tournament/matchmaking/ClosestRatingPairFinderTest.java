package es.us.dp1.chess.tournament.matchmaking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import es.us.dp1.chess.tournament.challenge.matchmaking.ClosestRatingPairFinder;
import es.us.dp1.chess.tournament.challenge.matchmaking.ValidClosestRatingPairFinder;

import static org.junit.jupiter.api.Assertions.fail;


public class ClosestRatingPairFinderTest {

    // This is your SUT:
    ClosestRatingPairFinder algorithm = new ValidClosestRatingPairFinder();

    // Please specify as many tests as you need using structures similar to this:
    @Test
    public void someTest(){
        // TODO: Remove next line and write your code here
        fail();
        // Arrangement / Configuration /Fixture
        // Act / SUT invocation
        //Assertion:
    }



    public void setAlgorithm(ClosestRatingPairFinder value){
        this.algorithm=value;
    }

}
