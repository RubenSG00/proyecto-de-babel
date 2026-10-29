package es.us.dp1.chess.tournament.practice.x.errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

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
public class TestX16 {

    @Autowired
    WebApplicationContext context;

    @Autowired
    VenueRepository repo;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX16CreatesAVenueWith201() throws Exception {
        mockMvc.perform(post("/api/v1/venues").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Auditorio Norte\", \"capacity\": 300}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Auditorio Norte"));
        assertEquals(3, repo.count(), "A new venue must be created (the id of the body is ignored)");
        assertEquals("Teatro Central", repo.findById(1).get().getName());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX16DuplicatedNameIs400() throws Exception {
        mockMvc.perform(post("/api/v1/venues").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"sala x\", \"capacity\": 10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists());
        assertEquals(2, repo.count());
    }

    @Test
    @WithMockUser(username = "player1", authorities = { "PLAYER" })
    public void testX16InvalidBodyIs400() throws Exception {
        mockMvc.perform(post("/api/v1/venues").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\", \"capacity\": 0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testX16SaveVenueRules() throws Exception {
        VenueRepository mockRepo = mock(VenueRepository.class);
        VenueService service = new VenueService(mockRepo);
        Venue existing = venue(2, "Sala X");
        when(mockRepo.findByNameIgnoreCase(any())).thenReturn(Optional.empty());
        when(mockRepo.findByNameIgnoreCase("SALA X")).thenReturn(Optional.of(existing));
        when(mockRepo.save(any(Venue.class))).thenAnswer(i -> i.getArgument(0));

        Venue duplicated = venue(null, "SALA X");
        assertThrows(DuplicatedVenueNameException.class, () -> service.saveVenue(duplicated));
        verify(mockRepo, never()).save(duplicated);

        // The same venue (same id) can be saved again with its own name
        Venue sameVenue = venue(2, "SALA X");
        assertDoesNotThrow(() -> service.saveVenue(sameVenue));
        Venue other = venue(null, "Nave 16");
        assertSame(other, service.saveVenue(other));

        Method m = VenueService.class.getDeclaredMethod("saveVenue", Venue.class);
        Transactional t = m.getAnnotation(Transactional.class);
        assertNotNull(t, "saveVenue must be transactional");
        assertTrue(Arrays.asList(t.rollbackFor()).contains(DuplicatedVenueNameException.class),
                "saveVenue must roll back when DuplicatedVenueNameException is thrown");
    }

    @Test
    public void testX16TheControllerDeclaresTheCheckedException() {
        boolean declares = Arrays.stream(VenueController.class.getDeclaredMethods())
                .anyMatch(m -> Arrays.asList(m.getExceptionTypes()).contains(DuplicatedVenueNameException.class));
        assertTrue(declares, "The POST method of VenueController must declare DuplicatedVenueNameException");
    }

    private static Venue venue(Integer id, String name) {
        Venue v = new Venue();
        v.setId(id);
        v.setName(name);
        v.setCapacity(100);
        return v;
    }

}
