package es.us.dp1.chess.tournament.practice.x.crud;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@SpringBootTest
@Transactional
public class TestX14 {

    @Autowired
    WebApplicationContext context;

    @Autowired
    TournamentRepository repo;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX14DeletesTheTournamentWith204() throws Exception {
        mockMvc.perform(delete("/api/v1/tournaments/3"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        assertFalse(repo.existsById(3), "The tournament must have been deleted");
        assertEquals(2, repo.count());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX14UnknownTournamentIs404() throws Exception {
        mockMvc.perform(delete("/api/v1/tournaments/474629")).andExpect(status().isNotFound());
        assertEquals(3, repo.count());
    }

}
