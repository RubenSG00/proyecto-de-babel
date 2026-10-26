package es.us.dp1.chess.tournament;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.us.dp1.chess.tournament.challenge.FriendlyChallenge;
import es.us.dp1.chess.tournament.challenge.FriendlyChallengeRepository;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Test7 {
    @Autowired
	private WebApplicationContext context;

	@Autowired
	private UserService userService;

	@MockitoBean
	private FriendlyChallengeRepository friendlyChallengeRepository;

	private MockMvc mockMvc;

    private String url = "/api/v1/challenges";
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
		.webAppContextSetup(context)
		.apply(SecurityMockMvcConfigurers.springSecurity())
		.build();

		User player1 = userService.findUser("player1");
		User player2 = userService.findUser("player2");

		FriendlyChallenge challenge1 = createChallenge(1, 2, player1, player2);
		FriendlyChallenge challenge2 = createChallenge(2, 0, player2);

		when(friendlyChallengeRepository.findById(1)).thenReturn(Optional.of(challenge1));
		when(friendlyChallengeRepository.findById(2)).thenReturn(Optional.of(challenge2));
		when(friendlyChallengeRepository.findById(474629)).thenReturn(Optional.empty());
		when(friendlyChallengeRepository.findParticipantPoints(1)).thenReturn(List.of(
			new Object[] { player1, 3L },
			new Object[] { player2, 1L }));
		when(friendlyChallengeRepository.findParticipantPoints(2)).thenReturn(List.of());
	}

	private FriendlyChallenge createChallenge(Integer id, Integer completedMatches, User... participants) {
		FriendlyChallenge challenge = new FriendlyChallenge();
		challenge.setId(id);
		challenge.setStartDate("2026-07-01");
		challenge.setEndDate("2026-07-02");
		challenge.setTotalMatches(2);
		challenge.setCompletedMatches(completedMatches);
		challenge.setParticipants(List.of(participants));
		return challenge;
	}



    @Test
    @Transactional
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void test7CanGetRankingAsParticipant() throws JsonProcessingException, Exception{
        mockMvc.perform(get(url+"/1/ranking"))
			.andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].user.id").value(4))
            .andExpect(jsonPath("$[0].points").value(3))
            .andExpect(jsonPath("$[1].user.id").value(5))
            .andExpect(jsonPath("$[1].points").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void test7CannotGetRankingOfNonExistentChallenge() throws JsonProcessingException, Exception{
       mockMvc.perform(get(url+"/474629/ranking"))
			.andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    public void test7CannotGetRankingWithoutFinishedMatches() throws JsonProcessingException, Exception{
       mockMvc.perform(get(url+"/2/ranking"))
			.andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "player4", authorities = {"PLAYER"})
    public void test7CannotGetRankingIfNotParticipant() throws JsonProcessingException, Exception{
       mockMvc.perform(get(url+"/1/ranking"))
			.andExpect(status().isForbidden());
    }

}
