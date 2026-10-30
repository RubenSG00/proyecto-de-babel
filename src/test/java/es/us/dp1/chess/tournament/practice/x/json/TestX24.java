package es.us.dp1.chess.tournament.practice.x.json;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class TestX24 {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX24NovelWithItsWriter() throws Exception {
        mockMvc.perform(get("/api/v1/novels/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Los pazos de Ulloa"))
                .andExpect(jsonPath("$.writer.id").value(2))
                .andExpect(jsonPath("$.writer.name").value("Emilia Pardo Bazan"))
                .andExpect(jsonPath("$.writer.novels").doesNotExist());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX24WriterWithoutNovels() throws Exception {
        mockMvc.perform(get("/api/v1/writers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Benito Perez Galdos"))
                .andExpect(jsonPath("$.novels").doesNotExist());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX24NovelsOfAWriter() throws Exception {
        mockMvc.perform(get("/api/v1/writers/1/novels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Fortunata y Jacinta", "Misericordia")))
                .andExpect(jsonPath("$[0].writer.id").value(1));
        mockMvc.perform(get("/api/v1/writers/474629/novels")).andExpect(status().isNotFound());
    }

}
