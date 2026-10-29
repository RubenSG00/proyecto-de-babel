package es.us.dp1.chess.tournament.practice.x.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

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

@SpringBootTest
@Transactional
public class TestX11 {

    @Autowired
    WebApplicationContext context;

    @Autowired
    SuggestionRepository repo;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testX11ValidSuggestionIsCreated() throws Exception {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        String body = "{\"id\": 77, \"title\": \"More blitz rounds\", \"text\": \"Please add a blitz tournament on Fridays\","
                + " \"createdAt\": \"2000-01-01T00:00:00\", \"author\": {\"id\": 1}}";
        mockMvc.perform(post("/api/v1/suggestions").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("More blitz rounds"))
                .andExpect(jsonPath("$.author.username").value("player2"));
        List<Suggestion> all = repo.findAll();
        assertEquals(1, all.size(), "Exactly one suggestion must have been saved");
        Suggestion s = all.get(0);
        assertNotEquals(77, s.getId(), "The id of the body must be ignored");
        assertEquals("player2", s.getAuthor().getUsername(), "The author must be the logged-in user");
        assertTrue(s.getCreatedAt() != null && s.getCreatedAt().isAfter(before),
                "createdAt must be the current instant, not the one in the body");
    }

    @Test
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testX11InvalidSuggestionIsABadRequest() throws Exception {
        for (String body : new String[] { "{\"title\": \"Hi\", \"text\": \"Too short title\"}",
                "{\"title\": \"A valid title\", \"text\": \"   \"}", "{\"text\": \"No title at all\"}" }) {
            mockMvc.perform(post("/api/v1/suggestions").with(csrf())
                    .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        assertTrue(repo.findAll().isEmpty(), "Invalid suggestions must not be saved");
    }

}
