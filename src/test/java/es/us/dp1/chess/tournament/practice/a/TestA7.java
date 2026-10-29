package es.us.dp1.chess.tournament.practice.a;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestA7 {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @MockitoBean
    private TrainingProgramRepository trainingProgramRepository;

    private MockMvc mockMvc;

    private String url = "/api/v1/gym/programs";

    private User player1, player2, player3;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        player1 = userService.findUser("player1");
        player2 = userService.findUser("player2");
        player3 = userService.findUser("player3");
        mockRepository(trainingProgramRepository);
    }

    private void mockRepository(TrainingProgramRepository repo) {
        TrainingProgram program1 = createProgram(1, player1, player2, player3);
        TrainingProgram program2 = createProgram(2, player2, player3);
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        when(repo.findById(1)).thenReturn(Optional.of(program1));
        when(repo.findById(2)).thenReturn(Optional.of(program2));
        when(repo.existsById(1)).thenReturn(true);
        when(repo.existsById(2)).thenReturn(true);
        when(repo.findMemberPoints(anyInt())).thenReturn(List.of());
        when(repo.findMemberPoints(1)).thenReturn(List.of(
                new Object[] { player2, 7L },
                new Object[] { player1, 4L },
                new Object[] { player3, 0L }));
    }

    private TrainingProgram createProgram(Integer id, User... members) {
        TrainingProgram p = new TrainingProgram();
        p.setId(id);
        p.setName("Program " + id);
        p.setStartDate(LocalDate.of(2026, 9, 1));
        p.setEndDate(LocalDate.of(2026, 9, 30));
        p.setPlannedSessions(3);
        p.setMembers(List.of(members));
        return p;
    }

    @Test
    @Transactional
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testA7CanGetLeaderboardAsMember() throws Exception {
        mockMvc.perform(get(url + "/1/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].user.id").value(5))
                .andExpect(jsonPath("$[0].points").value(7))
                .andExpect(jsonPath("$[1].user.id").value(4))
                .andExpect(jsonPath("$[1].points").value(4))
                .andExpect(jsonPath("$[2].user.id").value(6))
                .andExpect(jsonPath("$[2].points").value(0));
    }

    @Test
    @Transactional
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testA7CannotGetLeaderboardOfNonExistentProgram() throws Exception {
        mockMvc.perform(get(url + "/474629/leaderboard")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testA7CannotGetLeaderboardWithoutFinishedSessions() throws Exception {
        mockMvc.perform(get(url + "/2/leaderboard")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "player4", authorities = { "PLAYER" })
    public void testA7CannotGetLeaderboardIfNotMember() throws Exception {
        mockMvc.perform(get(url + "/1/leaderboard")).andExpect(status().isForbidden());
    }

    @Test
    public void testA7ServiceComputesTheLeaderboardWithItsRepository() {
        TrainingProgramRepository repo = mock(TrainingProgramRepository.class);
        UserService users = mock(UserService.class);
        mockRepository(repo);
        // A different instance with the same id: members must be compared by id
        User sameAsPlayer3 = new User();
        sameAsPlayer3.setId(player3.getId());
        when(users.findCurrentUser()).thenReturn(sameAsPlayer3);

        List<LeaderboardEntry> leaderboard = new TrainingProgramService(repo, users).getLeaderboard(1);

        assertNotNull(leaderboard, "getLeaderboard must not return null");
        assertEquals(3, leaderboard.size(), "The leaderboard must contain one entry per row returned by the repository");
        assertEquals(player2, leaderboard.get(0).getUser());
        assertEquals(7L, leaderboard.get(0).getPoints().longValue());
        assertEquals(player1, leaderboard.get(1).getUser());
        assertEquals(player3, leaderboard.get(2).getUser());
        assertEquals(0L, leaderboard.get(2).getPoints().longValue());
        verify(repo, atLeastOnce()).findMemberPoints(1);
        verify(users, atLeastOnce()).findCurrentUser();
    }

    @Test
    public void testA7ServiceEnforcesTheBusinessRules() {
        TrainingProgramRepository repo = mock(TrainingProgramRepository.class);
        UserService users = mock(UserService.class);
        mockRepository(repo);
        TrainingProgramService service = new TrainingProgramService(repo, users);

        when(users.findCurrentUser()).thenReturn(player2);
        assertThrows(ResourceNotFoundException.class, () -> service.getLeaderboard(474629),
                "getLeaderboard must throw ResourceNotFoundException if the program does not exist");
        assertThrows(ResourceNotFoundException.class, () -> service.getLeaderboard(2),
                "getLeaderboard must throw ResourceNotFoundException if no session of the program has finished");

        when(users.findCurrentUser()).thenReturn(userService.findUser("player4"));
        assertThrows(AccessDeniedException.class, () -> service.getLeaderboard(1),
                "getLeaderboard must throw AccessDeniedException if the current user is not a member");
    }

}
