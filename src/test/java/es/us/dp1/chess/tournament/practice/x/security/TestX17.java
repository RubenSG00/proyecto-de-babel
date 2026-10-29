package es.us.dp1.chess.tournament.practice.x.security;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@SpringBootTest
@Transactional
public class TestX17 {

    @Autowired
    WebApplicationContext context;

    @Autowired
    UserService userService;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX17APlayerSeesOnlyTheirExpenses() throws Exception {
        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)));
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testX17AnAdminSeesEverything() throws Exception {
        mockMvc.perform(get("/api/v1/expenses")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(5)));
        mockMvc.perform(get("/api/v1/expenses/3")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(3));
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX17OneExpense() throws Exception {
        mockMvc.perform(get("/api/v1/expenses/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
        mockMvc.perform(get("/api/v1/expenses/3")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/expenses/474629")).andExpect(status().isNotFound());
    }

    @Test
    public void testX17TheControllerDoesNotCheckAccess() {
        for (Field f : ExpenseController.class.getDeclaredFields())
            assertFalse(UserService.class.isAssignableFrom(f.getType()),
                    "ExpenseController must not use UserService: the access checks belong to the service");
    }

    @Test
    public void testX17OwnersAreComparedById() {
        ExpenseRepository repo = mock(ExpenseRepository.class);
        UserService users = mock(UserService.class);
        User player1 = userService.findUser("player1");
        Expense e = new Expense();
        e.setId(1);
        e.setOwner(player1);
        when(repo.findById(1)).thenReturn(Optional.of(e));
        when(repo.findByOwner(any())).thenReturn(List.of(e));
        User sameAsPlayer1 = new User();
        sameAsPlayer1.setId(player1.getId());
        sameAsPlayer1.setUsername(player1.getUsername());
        sameAsPlayer1.setAuthority(player1.getAuthority());
        when(users.findCurrentUser()).thenReturn(sameAsPlayer1);
        assertSame(e, new ExpenseService(repo, users).findExpense(1),
                "The owner of the expense is the current user, although it is a different object");
    }

}
