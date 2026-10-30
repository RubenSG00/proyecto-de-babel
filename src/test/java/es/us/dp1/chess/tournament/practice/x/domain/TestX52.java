package es.us.dp1.chess.tournament.practice.x.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

public class TestX52 {

    static GymMembership membership(LocalDate end, String fee) {
        GymMembership m = new GymMembership();
        m.setEndDate(end);
        m.setMonthlyFee(new BigDecimal(fee));
        m.setCancelled(false);
        return m;
    }

    @Test
    public void testX52Renew() {
        GymMembership m = membership(LocalDate.of(2026, 12, 31), "40");
        m.renew(3);
        assertEquals(LocalDate.of(2027, 3, 31), m.getEndDate());
        assertThrows(IllegalArgumentException.class, () -> m.renew(0));
        m.cancel();
        assertTrue(m.getCancelled());
        assertThrows(IllegalStateException.class, () -> m.renew(1));
    }

    @Test
    public void testX52EarlyTerminationFee() {
        GymMembership m = membership(LocalDate.of(2027, 1, 15), "40");
        // From 2026-10-01: 3 full months remain (until 2027-01-01), half of 40 each
        assertEquals(0, new BigDecimal("60").compareTo(m.calculateEarlyTerminationFee(LocalDate.of(2026, 10, 1))));
        assertEquals(0, BigDecimal.ZERO.compareTo(m.calculateEarlyTerminationFee(LocalDate.of(2027, 1, 1))));
        assertEquals(0, BigDecimal.ZERO.compareTo(m.calculateEarlyTerminationFee(LocalDate.of(2027, 2, 1))));
        // Only FULL months count: from 2026-10-02 to 2027-01-01 there are 2 full months, not 3
        GymMembership almost = membership(LocalDate.of(2027, 1, 1), "40");
        assertEquals(0, new BigDecimal("40").compareTo(almost.calculateEarlyTerminationFee(LocalDate.of(2026, 10, 2))));
        GymMembership odd = membership(LocalDate.of(2026, 12, 1), "25");
        assertEquals(0, new BigDecimal("12.5").compareTo(odd.calculateEarlyTerminationFee(LocalDate.of(2026, 11, 1))));
    }

    @Test
    public void testX52TheServiceCoordinates() throws NoSuchMethodException {
        GymMembershipRepository repo = mock(GymMembershipRepository.class);
        GymMembership m = membership(LocalDate.of(2027, 1, 15), "40");
        when(repo.findById(1)).thenReturn(Optional.of(m));
        when(repo.findById(2)).thenReturn(Optional.empty());
        when(repo.save(any(GymMembership.class))).thenAnswer(i -> i.getArgument(0));
        GymMembershipService service = new GymMembershipService(repo);

        assertSame(m, service.renew(1, 1));
        assertEquals(LocalDate.of(2027, 2, 15), m.getEndDate());
        verify(repo).save(m);
        assertThrows(ResourceNotFoundException.class, () -> service.renew(2, 1));

        BigDecimal fee = service.cancel(1, LocalDate.of(2026, 10, 1));
        assertEquals(0, new BigDecimal("80").compareTo(fee), "4 full months remain until 2027-02-15");
        assertTrue(m.getCancelled());
        assertThrows(ResourceNotFoundException.class, () -> service.cancel(2, LocalDate.of(2026, 10, 1)));

        for (Method method : new Method[] { GymMembershipService.class.getDeclaredMethod("renew", Integer.class, int.class),
                GymMembershipService.class.getDeclaredMethod("cancel", Integer.class, LocalDate.class) })
            assertNotNull(method.getAnnotation(Transactional.class), method.getName() + " must be @Transactional");
    }

}
