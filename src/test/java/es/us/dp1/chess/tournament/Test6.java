package es.us.dp1.chess.tournament;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.challenge.FriendlyChallengeRepository;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.EntityManager;

@DataJpaTest
public class Test6 {

    @Autowired
    FriendlyChallengeRepository fcr;

    @Autowired
    EntityManager em;

    @Test
    void testParticipantPointsForChallenge1() {
        // GIVEN: el desafío con ID=1 está precargado mediante data.sql, con dos
        // partidas finalizadas: la 15 (ganador: usuario 4) y la 20 (empate)
        Map<Integer, Integer> points = participantPointsAsMap(1);

        // THEN: el usuario 4 gana 2 puntos por la 15 y 1 por el empate de la 20; el
        // usuario 5 solo gana 1 punto, por el empate de la 20
        assertThat(points.get(4)).isEqualTo(3);
        assertThat(points.get(5)).isEqualTo(1);
        assertThat(points).hasSize(2);
    }

    @Test
    void testParticipantPointsForChallengeWithoutFinishedMatches() {
        // GIVEN: el desafío con ID=2 solo tiene una partida (21) sin finalizar
        Integer challengeId = 2;

        List<Object[]> result = fcr.findParticipantPoints(challengeId);

        // No hay partidas finalizadas → la lista debe ser vacía
        assertThat(result).isEmpty();
    }

    @Test
    void testParticipantPointsForChallenge3ComputesWinDrawAndLoss() {
        // GIVEN: el desafío con ID=3 tiene dos partidas finalizadas (22: gana el
        // usuario 4 al 6; 23: empatan 4 y 7) y una sin finalizar (24: entre 6 y 7)
        Map<Integer, Integer> points = participantPointsAsMap(3);

        // THEN: 4 gana 2 puntos por la 22 y 1 por el empate de la 23 (total 3);
        // 6 pierde la 22 y no gana nada (0 puntos, no negativos ni ausente);
        // 7 solo gana 1 punto, por el empate de la 23
        assertThat(points.get(4)).isEqualTo(3);
        assertThat(points.get(6)).isEqualTo(0);
        assertThat(points.get(7)).isEqualTo(1);
        assertThat(points).hasSize(3);
    }

    @Test
    void testParticipantPointsForChallenge3IgnoresUnfinishedMatches() {
        // GIVEN: la partida 24 del desafío 3 no está finalizada y termina en tablas
        // (winner nulo); si no se filtrase por "finish IS NOT NULL" contaría como un
        // empate adicional para los usuarios 6 y 7
        Map<Integer, Integer> points = participantPointsAsMap(3);

        int totalPointsAwarded = points.values().stream().mapToInt(Integer::intValue).sum();

        // Solo las partidas 22 (2 puntos) y 23 (1+1 puntos) deben contar: 4 en total
        assertThat(totalPointsAwarded).isEqualTo(4);
    }

    @Test
    void testParticipantPointsForChallenge3IsSortedByPointsDescending() {
        // GIVEN: el desafío 3 tiene tres participantes con puntuaciones distintas
        // (4:3, 7:1, 6:0), sin empates, para poder comprobar el orden sin ambigüedad
        List<Object[]> result = fcr.findParticipantPoints(3);

        assertThat(result).hasSize(3);
        assertThat(((User) result.get(0)[0]).getId()).isEqualTo(4);
        assertThat(((Number) result.get(0)[1]).intValue()).isEqualTo(3);
        assertThat(((User) result.get(1)[0]).getId()).isEqualTo(7);
        assertThat(((Number) result.get(1)[1]).intValue()).isEqualTo(1);
        assertThat(((User) result.get(2)[0]).getId()).isEqualTo(6);
        assertThat(((Number) result.get(2)[1]).intValue()).isEqualTo(0);
    }

    private Map<Integer, Integer> participantPointsAsMap(Integer challengeId) {
        List<Object[]> result = fcr.findParticipantPoints(challengeId);
        Map<Integer, Integer> points = new HashMap<>();
        for (Object[] row : result) {
            Integer userId = ((User) row[0]).getId();
            Integer p = ((Number) row[1]).intValue();
            points.put(userId, p);
        }
        return points;
    }

}
