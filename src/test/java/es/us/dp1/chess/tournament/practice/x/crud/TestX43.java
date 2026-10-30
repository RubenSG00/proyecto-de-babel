package es.us.dp1.chess.tournament.practice.x.crud;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import es.us.dp1.chess.tournament.developers.DevelopersController;

@SpringBootTest
public class TestX43 {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void testX43DevelopersControllerIsARestController() {
        assertNotNull(DevelopersController.class.getAnnotation(RestController.class),
                "DevelopersController must be annotated with @RestController");
    }

    @Test
    public void testX43DevelopersArePublic() throws Exception {
        // No @WithMockUser: the endpoint must answer without authentication
        mockMvc.perform(get("/api/v1/developers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("JAParejo"))
                .andExpect(jsonPath("$[0].email").value("japarejo@gmail.com"))
                .andExpect(jsonPath("$[0].url").value("http://www.isa.us.es"))
                .andExpect(jsonPath("$[0].roles").isArray())
                .andExpect(jsonPath("$[0].properties").exists());
    }

}
