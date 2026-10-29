package es.us.dp1.chess.tournament.practice.x.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class TestX12 {

    @Autowired
    WebApplicationContext context;

    @Autowired
    RecipeRepository repo;

    @Autowired
    EntityManager em;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX12CreatesANewRecipeOwnedByTheCurrentUser() throws Exception {
        mockMvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Tortilla\", \"minutes\": 30}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tortilla"))
                .andExpect(jsonPath("$.minutes").value(30))
                .andExpect(jsonPath("$.owner.username").value("player1"))
                .andExpect(jsonPath("$.featured").value(false));
        assertEquals(3, repo.count());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX12CannotOverwriteAnotherRecipe() throws Exception {
        // Recipe 2 (Gazpacho) belongs to user 5: sending its id must not modify it
        mockMvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 2, \"name\": \"Hacked\", \"minutes\": 1}"))
                .andExpect(status().isCreated());
        em.flush();
        em.clear();
        Recipe gazpacho = repo.findById(2).get();
        assertEquals("Gazpacho", gazpacho.getName(), "Recipe 2 was overwritten through the id of the body");
        assertEquals(5, gazpacho.getOwner().getId());
        assertEquals(3, repo.count(), "A new recipe must be created");
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX12CannotChooseOwnerNorFeatured() throws Exception {
        mockMvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Paella\", \"minutes\": 60, \"featured\": true, \"owner\": {\"id\": 5}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.owner.username").value("player1"))
                .andExpect(jsonPath("$.featured").value(false));
    }

}
