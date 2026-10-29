package es.us.dp1.chess.tournament.practice.x.crud;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
public class TestX13 {

    static final String VALID = "{\"name\": \"Seville Open 2027\", \"description\": \"Classical tournament\","
            + " \"logoUrl\": null, \"prizePool\": 6000, \"format\": \"ROUND_ROBIN\"}";

    @Autowired
    WebApplicationContext context;

    @Autowired
    TournamentRepository repo;

    @Autowired
    EntityManager em;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX13UpdatesTheTournament() throws Exception {
        mockMvc.perform(put("/api/v1/tournaments/1").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Seville Open 2027"))
                .andExpect(jsonPath("$.format").value("ROUND_ROBIN"));
        em.flush();
        em.clear();
        Tournament t = repo.findById(1).get();
        assertEquals("Seville Open 2027", t.getName());
        assertEquals(0, new BigDecimal("6000").compareTo(t.getPrizePool()));
        assertNull(t.getLogoUrl());
        assertEquals(3, repo.count(), "Updating must not create a new tournament");
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX13TheIdOfTheUrlWins() throws Exception {
        String withOtherId = VALID.replace("{", "{\"id\": 2, ");
        mockMvc.perform(put("/api/v1/tournaments/1").contentType(MediaType.APPLICATION_JSON).content(withOtherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        em.flush();
        em.clear();
        assertEquals("Blitz Masters", repo.findById(2).get().getName(), "Tournament 2 must not be modified");
        assertEquals("Seville Open 2027", repo.findById(1).get().getName());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX13NotFoundAndBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/tournaments/474629").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isNotFound());
        mockMvc.perform(put("/api/v1/tournaments/1").contentType(MediaType.APPLICATION_JSON)
                .content(VALID.replace("Seville Open 2027", "SO")))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/v1/tournaments/1").contentType(MediaType.APPLICATION_JSON)
                .content(VALID.replace("6000", "-1")))
                .andExpect(status().isBadRequest());
    }

}
