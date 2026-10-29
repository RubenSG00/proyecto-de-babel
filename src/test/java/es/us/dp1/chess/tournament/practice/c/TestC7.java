package es.us.dp1.chess.tournament.practice.c;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.repository.Repository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;
import jakarta.persistence.EntityManager;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestC7 {

    private static final String URL = "/api/v1/airline/bases";

    @Autowired
    private WebApplicationContext context;

    @MockitoSpyBean
    private UserService userService;

    @MockitoSpyBean
    private CrewBaseService crewBaseService;

    @MockitoBean
    private CrewBaseRepository crewBaseRepository;

    private MockMvc mockMvc;

    private User player1, player2, player3;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        player1 = userService.findUser("player1");
        player2 = userService.findUser("player2");
        player3 = userService.findUser("player3");
        mockRepository(crewBaseRepository);
        clearInvocations(userService, crewBaseService, crewBaseRepository);
    }

    private void mockRepository(CrewBaseRepository repo) {
        CrewBase base1 = createBase(1, List.of(flight(true, false), flight(false, false)), player1, player2, player3);
        // Base 2: one flight in the air and one diverted, none counts
        CrewBase base2 = createBase(2, List.of(flight(false, false), flight(true, true)), player1, player2);
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        when(repo.findById(1)).thenReturn(Optional.of(base1));
        when(repo.findById(2)).thenReturn(Optional.of(base2));
        when(repo.existsById(anyInt())).thenAnswer(i -> List.of(1, 2).contains((Integer) i.getArgument(0)));
        when(repo.findPilotScores(anyInt(), anyInt())).thenReturn(List.of());
        when(repo.findPilotScores(1, 0)).thenReturn(List.of(new Object[] { player2, 7L },
                new Object[] { player1, 5L }, new Object[] { player3, 1L }));
        when(repo.findPilotScores(1, 5)).thenReturn(List.of(new Object[] { player2, 7L },
                new Object[] { player1, 5L }));
    }

    private static Flight flight(boolean landed, boolean diverted) {
        Flight f = new Flight();
        f.setDeparture(LocalDateTime.of(2026, 7, 1, 8, 0));
        f.setArrival(landed ? LocalDateTime.of(2026, 7, 1, 10, 0) : null);
        f.setDiverted(diverted);
        f.setDistanceKm(1000);
        return f;
    }

    private CrewBase createBase(Integer id, List<Flight> flights, User... pilots) {
        CrewBase b = new CrewBase();
        b.setId(id);
        b.setName("Base " + id);
        b.setCity("City " + id);
        b.setPilots(List.of(pilots));
        b.setFlights(flights);
        return b;
    }

    // ---------------------------------------------------------------- behaviour

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testC7RankingWithoutMinScoreUsesZero() throws Exception {
        mockMvc.perform(get(URL + "/1/pilot-ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].pilot.id").value(5))
                .andExpect(jsonPath("$[0].score").value(7))
                .andExpect(jsonPath("$[2].pilot.id").value(6));
        verify(crewBaseRepository).findPilotScores(1, 0);
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testC7RankingWithMinScore() throws Exception {
        mockMvc.perform(get(URL + "/1/pilot-ranking").param("minScore", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        mockMvc.perform(get(URL + "/1/pilot-ranking").param("minScore", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testC7NegativeMinScoreIsABadRequest() throws Exception {
        mockMvc.perform(get(URL + "/1/pilot-ranking").param("minScore", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists());
        // The threshold is checked first, even for a base that does not exist
        mockMvc.perform(get(URL + "/474629/pilot-ranking").param("minScore", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testC7NotFound() throws Exception {
        mockMvc.perform(get(URL + "/474629/pilot-ranking")).andExpect(status().isNotFound());
        mockMvc.perform(get(URL + "/2/pilot-ranking")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "player4", authorities = { "PLAYER" })
    public void testC7NonPilotIsForbidden() throws Exception {
        mockMvc.perform(get(URL + "/1/pilot-ranking")).andExpect(status().isForbidden());
        // Access (403) is checked before the flights (404)
        mockMvc.perform(get(URL + "/2/pilot-ranking")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testC7AdminCanSeeAnyBase() throws Exception {
        mockMvc.perform(get(URL + "/1/pilot-ranking")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    // ---------------------------------------------------------------- layers

    @Test
    public void testC7ControllerOnlyDependsOnTheService() {
        boolean hasService = false;
        for (Field f : CrewBaseController.class.getDeclaredFields()) {
            Class<?> t = f.getType();
            assertFalse(Repository.class.isAssignableFrom(t) || EntityManager.class.isAssignableFrom(t),
                    "CrewBaseController must not depend on " + t.getSimpleName());
            assertFalse(UserService.class.isAssignableFrom(t),
                    "CrewBaseController must not use UserService: checking the access is business logic");
            hasService |= CrewBaseService.class.isAssignableFrom(t);
        }
        assertTrue(hasService, "CrewBaseController must use CrewBaseService");
    }

    @Test
    public void testC7TransactionsBelongToTheService() throws NoSuchMethodException {
        assertNull(CrewBaseController.class.getAnnotation(Transactional.class),
                "CrewBaseController must not be transactional");
        for (Method m : CrewBaseController.class.getDeclaredMethods())
            assertNull(m.getAnnotation(Transactional.class), "The controller method " + m.getName()
                    + " must not be transactional");
        assertNotNull(CrewBaseService.class.getDeclaredMethod("getPilotRanking", Integer.class, Integer.class)
                .getAnnotation(Transactional.class), "getPilotRanking must be a transactional method of the service");
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testC7ControllerDelegatesOnTheService() throws Exception {
        doReturn(List.of(new PilotScore(player3, 9L))).when(crewBaseService).getPilotRanking(1, 3);
        clearInvocations(crewBaseService);
        mockMvc.perform(get(URL + "/1/pilot-ranking").param("minScore", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].pilot.id").value(player3.getId()))
                .andExpect(jsonPath("$[0].score").value(9));
        verify(crewBaseService).getPilotRanking(1, 3);
        verifyNoInteractions(crewBaseRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testC7ControllerLetsExceptionsPropagate() throws Exception {
        doThrow(new ResourceNotFoundException("CrewBase", "id", 1)).when(crewBaseService).getPilotRanking(eq(1), anyInt());
        doThrow(new AccessDeniedException("Not a pilot")).when(crewBaseService).getPilotRanking(eq(2), anyInt());
        mockMvc.perform(get(URL + "/1/pilot-ranking")).andExpect(status().isNotFound());
        mockMvc.perform(get(URL + "/2/pilot-ranking")).andExpect(status().isForbidden());
    }

    // ---------------------------------------------------------------- service

    @Test
    public void testC7ServiceRules() {
        CrewBaseRepository repo = mock(CrewBaseRepository.class);
        UserService users = mock(UserService.class);
        mockRepository(repo);
        CrewBaseService service = new CrewBaseService(repo, users);

        assertThrows(InvalidScoreThresholdException.class, () -> service.getPilotRanking(1, -1));
        verify(repo, never()).findById(anyInt());

        User sameAsPlayer3 = new User();
        sameAsPlayer3.setId(player3.getId());
        sameAsPlayer3.setAuthority(player3.getAuthority());
        when(users.findCurrentUser()).thenReturn(sameAsPlayer3);
        List<PilotScore> ranking = service.getPilotRanking(1, 0);
        assertEquals(3, ranking.size());
        assertEquals(player2, ranking.get(0).getPilot());
        assertEquals(7L, ranking.get(0).getScore());
        assertTrue(service.getPilotRanking(1, 50).isEmpty(), "A high minScore gives an empty ranking, not a 404");
        assertThrows(ResourceNotFoundException.class, () -> service.getPilotRanking(474629, 0));
    }

}
