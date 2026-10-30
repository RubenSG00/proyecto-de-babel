package es.us.dp1.chess.tournament.practice.x.crud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class TestX42 {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX42PlayersCanUseTheTournamentsApi() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/tournaments/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testX42OtherAuthoritiesAreForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/tournaments/1")).andExpect(status().isForbidden());
    }

    @Test
    public void testX42AnonymousRequestsAreUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/tournaments")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/tournaments/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testX42TheOtherRulesDoNotChange() throws Exception {
        // An authenticated ADMIN can still use the rest of the API...
        mockMvc.perform(get("/api/v1/venues/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/recipes/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX42UsersApiIsStillForAdmins() throws Exception {
        mockMvc.perform(get("/api/v1/users")).andExpect(status().isForbidden());
    }

}
