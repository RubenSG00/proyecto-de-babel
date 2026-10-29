package es.us.dp1.chess.tournament.practice.b;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.web.context.WebApplicationContext;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

// The requests are NOT wrapped in a test transaction: the user returned by findCurrentUser()
// is a different instance from the members of the mocked clubs.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestB7 {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @MockitoBean
    private ReadingClubRepository readingClubRepository;

    private MockMvc mockMvc;

    private String url = "/api/v1/library/clubs";

    private User player1, player2, player3;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        player1 = userService.findUser("player1");
        player2 = userService.findUser("player2");
        player3 = userService.findUser("player3");
        mockRepository(readingClubRepository);
    }

    private void mockRepository(ReadingClubRepository repo) {
        ReadingClub club1 = createClub(1, List.of(new BookLoan(), new BookLoan()), player1, player2, player3);
        ReadingClub club2 = createClub(2, null, player2, player3);
        ReadingClub club3 = createClub(3, List.of(), player1, player2);
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        when(repo.findById(1)).thenReturn(Optional.of(club1));
        when(repo.findById(2)).thenReturn(Optional.of(club2));
        when(repo.findById(3)).thenReturn(Optional.of(club3));
        when(repo.existsById(anyInt())).thenAnswer(i -> List.of(1, 2, 3).contains((Integer) i.getArgument(0)));
        when(repo.findOverdueCounts(anyInt(), any())).thenReturn(List.of());
        when(repo.findOverdueCounts(eq(1), any())).thenReturn(List.of(
                new Object[] { player3, 2L },
                new Object[] { player1, 1L },
                new Object[] { player2, 0L }));
    }

    private ReadingClub createClub(Integer id, List<BookLoan> loans, User... members) {
        ReadingClub c = new ReadingClub();
        c.setId(id);
        c.setName("Club " + id);
        c.setMaxMembers(10);
        c.setMembers(List.of(members));
        c.setLoans(loans);
        return c;
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testB7CanGetReportAsMember() throws Exception {
        mockMvc.perform(get(url + "/1/overdue-report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].user.id").value(6))
                .andExpect(jsonPath("$[0].overdueLoans").value(2))
                .andExpect(jsonPath("$[1].user.id").value(4))
                .andExpect(jsonPath("$[2].user.id").value(5))
                .andExpect(jsonPath("$[2].overdueLoans").value(0));
        verify(readingClubRepository).findOverdueCounts(1, LocalDate.now());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testB7AdminCanGetTheReportOfAnyClub() throws Exception {
        mockMvc.perform(get(url + "/1/overdue-report")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testB7NonExistentClub() throws Exception {
        mockMvc.perform(get(url + "/474629/overdue-report")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "player4", authorities = { "PLAYER" })
    public void testB7NonMemberWithoutAdminIsForbidden() throws Exception {
        mockMvc.perform(get(url + "/1/overdue-report")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testB7ClubWithoutLoans() throws Exception {
        mockMvc.perform(get(url + "/2/overdue-report")).andExpect(status().isNotFound());
        mockMvc.perform(get(url + "/3/overdue-report")).andExpect(status().isNotFound());
    }

    @Test
    public void testB7ServiceChecksInTheRightOrder() {
        ReadingClubRepository repo = mock(ReadingClubRepository.class);
        UserService users = mock(UserService.class);
        mockRepository(repo);
        ReadingClubService service = new ReadingClubService(repo, users);

        // An outsider asking for a club without loans: the membership check (403) comes before the loans check (404)
        when(users.findCurrentUser()).thenReturn(userService.findUser("player4"));
        assertThrows(AccessDeniedException.class, () -> service.getOverdueReport(2));
        assertThrows(ResourceNotFoundException.class, () -> service.getOverdueReport(474629));

        User sameAsPlayer2 = new User();
        sameAsPlayer2.setId(player2.getId());
        sameAsPlayer2.setAuthority(player2.getAuthority());
        when(users.findCurrentUser()).thenReturn(sameAsPlayer2);
        List<OverdueEntry> report = service.getOverdueReport(1);
        assertEquals(3, report.size());
        assertEquals(player3, report.get(0).getUser());
        assertEquals(2L, report.get(0).getOverdueLoans());
    }

}
