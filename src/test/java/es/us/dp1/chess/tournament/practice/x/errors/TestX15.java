package es.us.dp1.chess.tournament.practice.x.errors;

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
public class TestX15 {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX15AvailableVenueIs200() throws Exception {
        mockMvc.perform(get("/api/v1/venues/1/availability").param("people", "50")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX15CapacityExceededIs400WithAnErrorMessage() throws Exception {
        mockMvc.perform(get("/api/v1/venues/1/availability").param("people", "51"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Venue Teatro Central only admits 50 people"))
                .andExpect(jsonPath("$.description").value("uri=/api/v1/venues/1/availability"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX15UnknownVenueIsStill404() throws Exception {
        mockMvc.perform(get("/api/v1/venues/474629/availability").param("people", "1"))
                .andExpect(status().isNotFound());
    }

}
