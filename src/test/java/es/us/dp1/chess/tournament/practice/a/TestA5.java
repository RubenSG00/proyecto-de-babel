package es.us.dp1.chess.tournament.practice.a;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.Test1;

@ExtendWith(MockitoExtension.class)
public class TestA5 extends ReflexiveTest {

    @Mock
    TrainingSessionRepository tsr;

    TrainingSessionService tss;

    private static final LocalDateTime START = LocalDateTime.of(2026, 9, 1, 8, 0);

    @BeforeEach
    public void configuration() {
        tss = new TrainingSessionService(tsr);
    }

    @Test
    public void testA5ServiceIsASpringService() {
        assertTrue(classIsAnnotatedWith(TrainingSessionService.class, Service.class),
                "TrainingSessionService is not annotated as a Spring service");
        assertNull(TrainingSessionService.class.getAnnotation(Transactional.class),
                "The transactional annotations must be placed at the method level, not the class level");
    }

    @Test
    public void testA5CheckTransactionality() throws NoSuchMethodException {
        checkTransactional(TrainingSessionService.class, "findAll");
        checkTransactional(TrainingSessionService.class, "findById", Integer.class);
        checkTransactional(TrainingSessionService.class, "save", TrainingSession.class);
        checkTransactionalRollback(TrainingSessionService.class, "save", Arrays.array(TrainingSession.class),
                InvalidSessionScheduleException.class);
        assertTrue(TrainingSessionService.class.getDeclaredMethod("findAll").getAnnotation(Transactional.class)
                .readOnly(), "findAll must be a read-only transaction");
        assertTrue(TrainingSessionService.class.getDeclaredMethod("findById", Integer.class)
                .getAnnotation(Transactional.class).readOnly(), "findById must be a read-only transaction");
        assertFalse(TrainingSessionService.class.getDeclaredMethod("save", TrainingSession.class)
                .getAnnotation(Transactional.class).readOnly(), "save writes, so it cannot be read-only");
    }

    @Test
    public void testA5CanGetAllSessions() {
        when(tsr.findAll()).thenReturn(List.of());
        List<TrainingSession> sessions = tss.findAll();
        assertNotNull(sessions, "The list of sessions found by the TrainingSessionService was null");
        verify(tsr).findAll();
    }

    @Test
    public void testA5CanGetSessionById() {
        TrainingSession s = new TrainingSession();
        when(tsr.findById(15)).thenReturn(Optional.of(s));
        Optional<TrainingSession> result = tss.findById(15);
        verify(tsr).findById(15);
        assertNotNull(result, "findById must not return null");
        assertSame(s, result.get());
    }

    @Test
    public void testA5CanSaveUnfinishedSession() {
        TrainingSession s = createSession(START, null);
        when(tsr.save(any(TrainingSession.class))).thenReturn(s);
        assertDoesNotThrow(() -> tss.save(s), "An unfinished session must be saved without exceptions");
        verify(tsr).save(s);
    }

    @Test
    public void testA5CanSaveSessionThatFinishesAfterItStarts() {
        TrainingSession s = createSession(START, START.plusMinutes(45));
        when(tsr.save(any(TrainingSession.class))).thenReturn(s);
        assertDoesNotThrow(() -> tss.save(s), "A session that finishes after it starts must be saved");
        verify(tsr).save(s);
    }

    @Test
    public void testA5CannotSaveSessionThatFinishesBeforeItStarts() {
        TrainingSession s = createSession(START, START.minusMinutes(1));
        assertThrows(InvalidSessionScheduleException.class, () -> tss.save(s));
        verify(tsr, never()).save(s);
    }

    @Test
    public void testA5CannotSaveSessionThatFinishesWhenItStarts() {
        TrainingSession s = createSession(START, START);
        assertThrows(InvalidSessionScheduleException.class, () -> tss.save(s),
                "finish must be strictly after start");
        verify(tsr, never()).save(s);
    }

    @Test
    public void testA5CannotSaveFinishedSessionWithoutStart() {
        TrainingSession s = createSession(null, START);
        assertThrows(InvalidSessionScheduleException.class, () -> tss.save(s));
        verify(tsr, never()).save(s);
    }

    private TrainingSession createSession(LocalDateTime start, LocalDateTime finish) {
        TrainingSession s = new TrainingSession();
        s.setCoach(Test1.createUser("coach"));
        s.setTrainee(Test1.createUser("trainee"));
        s.setStart(start);
        s.setFinish(finish);
        s.setDurationMinutes(45);
        return s;
    }

}
