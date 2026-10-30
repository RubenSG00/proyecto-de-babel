package es.us.dp1.chess.tournament.practice.x.json;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.Repository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.us.dp1.chess.tournament.user.UserService;

@SpringBootTest
public class TestX25 {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testX25TheCardHasOnlyThePublicData() throws Exception {
        mockMvc.perform(get("/api/v1/players/4/card"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("player1"))
                .andExpect(jsonPath("$.rating").value(1500))
                .andExpect(jsonPath("$.authority").value("PLAYER"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testX25UnknownUserIs404() throws Exception {
        mockMvc.perform(get("/api/v1/players/474629/card")).andExpect(status().isNotFound());
    }

    @Test
    public void testX25TheServiceUsesUserService() {
        boolean usesUserService = false;
        for (Field f : PlayerCardService.class.getDeclaredFields()) {
            assertFalse(Repository.class.isAssignableFrom(f.getType()),
                    "PlayerCardService must obtain the user through UserService, not through a repository");
            usesUserService |= UserService.class.isAssignableFrom(f.getType());
        }
        assertTrue(usesUserService, "PlayerCardService must use UserService");
    }

}
