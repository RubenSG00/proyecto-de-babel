package es.us.dp1.chess.tournament.practice.c;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.Test1;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;

@ExtendWith(MockitoExtension.class)
public class TestC5 extends ReflexiveTest {

    @Mock
    FlightRepository fr;

    FlightService fs;

    static final LocalDateTime DEPARTURE = LocalDateTime.of(2026, 7, 2, 8, 0);

    User captain, firstOfficer;
    Flight inTheAir, landed;

    @BeforeEach
    public void setup() {
        fs = new FlightService(fr);
        captain = user(4, "captain");
        firstOfficer = user(5, "officer");
        inTheAir = flight(DEPARTURE, null);
        landed = flight(DEPARTURE, DEPARTURE.plusHours(2));
        lenient().when(fr.findById(1)).thenReturn(Optional.of(inTheAir));
        lenient().when(fr.findById(2)).thenReturn(Optional.of(landed));
        lenient().when(fr.findById(99)).thenReturn(Optional.empty());
        lenient().when(fr.save(any(Flight.class))).thenAnswer(i -> i.getArgument(0));
    }

    private static User user(Integer id, String name) {
        User u = Test1.createUser(name);
        u.setId(id);
        return u;
    }

    private Flight flight(LocalDateTime departure, LocalDateTime arrival) {
        Flight f = new Flight();
        f.setCode("IB0001");
        f.setDistanceKm(1000);
        f.setDiverted(false);
        f.setCaptain(captain);
        f.setFirstOfficer(firstOfficer);
        f.setDeparture(departure);
        f.setArrival(arrival);
        return f;
    }

    private static List<Class<? extends Throwable>> rollbackFor(String name, Class<?>... types) throws NoSuchMethodException {
        Transactional t = FlightService.class.getDeclaredMethod(name, types).getAnnotation(Transactional.class);
        assertNotNull(t, "The method " + name + " is not annotated as transactional");
        return Arrays.asList(t.rollbackFor());
    }

    @Test
    public void testC5ServiceAndTransactions() throws NoSuchMethodException {
        assertTrue(classIsAnnotatedWith(FlightService.class, Service.class), "FlightService is not a Spring service");
        assertNull(FlightService.class.getAnnotation(Transactional.class),
                "The transactional annotations must be placed at the method level");
        for (Method m : new Method[] { FlightService.class.getDeclaredMethod("findAll"),
                FlightService.class.getDeclaredMethod("findById", Integer.class) }) {
            Transactional t = m.getAnnotation(Transactional.class);
            assertTrue(t != null && t.readOnly(), m.getName() + " must be a read-only transaction");
        }
        assertTrue(rollbackFor("save", Flight.class)
                .containsAll(List.of(InvalidCrewException.class, InvalidFlightTimesException.class)),
                "save must roll back for InvalidCrewException and InvalidFlightTimesException");
        assertTrue(rollbackFor("registerLanding", Integer.class, LocalDateTime.class, boolean.class)
                .containsAll(List.of(FlightAlreadyLandedException.class, InvalidFlightTimesException.class)),
                "registerLanding must roll back for FlightAlreadyLandedException and InvalidFlightTimesException");
    }

    @Test
    public void testC5FindAllAndFindById() {
        lenient().when(fr.findAll()).thenReturn(List.of(inTheAir, landed));
        assertEquals(List.of(inTheAir, landed), fs.findAll());
        Optional<Flight> found = fs.findById(2);
        assertNotNull(found, "findById must not return null");
        assertSame(landed, found.get());
        assertTrue(fs.findById(99).isEmpty());
    }

    @Test
    public void testC5SaveValidFlights() {
        assertDoesNotThrow(() -> fs.save(inTheAir));
        assertDoesNotThrow(() -> fs.save(landed));
        verify(fr).save(inTheAir);
        verify(fr).save(landed);
    }

    @Test
    public void testC5SaveRejectsAnInvalidCrew() {
        Flight noCaptain = flight(DEPARTURE, null);
        noCaptain.setCaptain(null);
        Flight noOfficer = flight(DEPARTURE, null);
        noOfficer.setFirstOfficer(null);
        Flight sameUser = flight(DEPARTURE, null);
        sameUser.setFirstOfficer(captain);
        Flight sameId = flight(DEPARTURE, null);
        sameId.setFirstOfficer(user(4, "same id, other object"));
        for (Flight f : List.of(noCaptain, noOfficer, sameUser, sameId))
            assertThrows(InvalidCrewException.class, () -> fs.save(f));
        verify(fr, never()).save(any(Flight.class));
    }

    @Test
    public void testC5SaveRejectsInvalidTimes() {
        Flight beforeDeparture = flight(DEPARTURE, DEPARTURE.minusMinutes(1));
        Flight atDeparture = flight(DEPARTURE, DEPARTURE);
        Flight noDeparture = flight(null, DEPARTURE);
        for (Flight f : List.of(beforeDeparture, atDeparture, noDeparture))
            assertThrows(InvalidFlightTimesException.class, () -> fs.save(f));
        verify(fr, never()).save(any(Flight.class));
    }

    @Test
    public void testC5RegisterLanding() throws Exception {
        Flight result = fs.registerLanding(1, DEPARTURE.plusHours(3), true);
        assertSame(inTheAir, result);
        assertEquals(DEPARTURE.plusHours(3), inTheAir.getArrival());
        assertTrue(inTheAir.getDiverted());
        verify(fr).save(inTheAir);
    }

    @Test
    public void testC5RegisterLandingErrors() {
        assertThrows(ResourceNotFoundException.class, () -> fs.registerLanding(99, DEPARTURE.plusHours(1), false));
        assertThrows(FlightAlreadyLandedException.class, () -> fs.registerLanding(2, DEPARTURE.plusHours(5), false));
        assertThrows(InvalidFlightTimesException.class, () -> fs.registerLanding(1, null, false));
        assertThrows(InvalidFlightTimesException.class, () -> fs.registerLanding(1, DEPARTURE, false));
        assertNull(inTheAir.getArrival(), "A rejected landing must not modify the flight");
        assertFalse(inTheAir.getDiverted(), "A rejected landing must not modify the flight");
        verify(fr, never()).save(any(Flight.class));
    }

}
