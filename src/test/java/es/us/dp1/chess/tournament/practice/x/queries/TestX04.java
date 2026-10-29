package es.us.dp1.chess.tournament.practice.x.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@DataJpaTest
@Import(ConcertService.class)
public class TestX04 {

    @MockitoSpyBean
    ConcertRepository repo;

    @Autowired
    ConcertService service;

    // Runs the service method and checks that it made exactly one call to the repository, to a
    // derived query method declared in ConcertRepository (no findAll, no @Query)
    private List<Concert> callOnce(Supplier<List<Concert>> serviceCall, String what) {
        clearInvocations(repo);
        List<Concert> result = serviceCall.get();
        assertNotNull(result, what + " returned null");
        verify(repo, never()).findAll();
        var invocations = mockingDetails(repo).getInvocations();
        assertEquals(1, invocations.size(), what + " must make exactly one call to the repository");
        Method called = ((Invocation) invocations.iterator().next()).getMethod();
        Method m;
        try {
            m = ConcertRepository.class.getMethod(called.getName(), called.getParameterTypes());
        } catch (NoSuchMethodException e) {
            fail(what + " called " + called.getName() + ", which is not a method of ConcertRepository");
            return null;
        }
        assertEquals(ConcertRepository.class, m.getDeclaringClass(),
                what + " must call a query method declared in ConcertRepository, not " + m.getName());
        assertTrue(m.getName().startsWith("find"), what + " must call a derived query method (find...By...)");
        assertNull(m.getAnnotation(Query.class), m.getName() + " must be a derived query, without @Query");
        return result;
    }

    private static List<Integer> ids(List<Concert> concerts) {
        return concerts.stream().map(Concert::getId).toList();
    }

    @Test
    public void testX04FindUpcomingInCity() {
        List<Concert> r = callOnce(() -> service.findUpcomingInCity("Seville", LocalDate.of(2026, 11, 5)),
                "findUpcomingInCity");
        // 1 and 8 are on 2026-11-05 itself: "after" is strict
        assertThat(ids(r)).containsExactly(3, 6);
    }

    @Test
    public void testX04FindAffordableNotSoldOut() {
        List<Concert> r = callOnce(() -> service.findAffordableNotSoldOut(40), "findAffordableNotSoldOut");
        // 6 costs exactly 40 (strictly lower is required); 2, 5 and 8 are sold out
        assertThat(ids(r)).containsExactlyInAnyOrder(1, 3, 7);
    }

    @Test
    public void testX04FindByGenreOrCity() {
        List<Concert> r = callOnce(() -> service.findByGenreOrCity(MusicGenre.JAZZ, "Valencia"), "findByGenreOrCity");
        assertThat(ids(r)).containsExactlyInAnyOrder(2, 4, 6);
    }

    @Test
    public void testX04FindStartingWith() {
        List<Concert> r = callOnce(() -> service.findStartingWith("Rock"), "findStartingWith");
        assertThat(r.stream().map(Concert::getTitle).toList()).containsExactly("Rock Night", "Rock Legends");
        List<Concert> lower = callOnce(() -> service.findStartingWith("rock"), "findStartingWith");
        assertThat(lower).as("the prefix is case-sensitive").isEmpty();
    }

}
