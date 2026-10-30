package es.us.dp1.chess.tournament.practice.x.logging;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class TestX48 {

    ListAppender<ILoggingEvent> appender;
    Logger logger;

    @BeforeEach
    public void captureLogs() {
        // The logger of the class (the one that Lombok's @Slf4j creates) receives a list appender
        logger = (Logger) LoggerFactory.getLogger(RegistrationAuditService.class);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    public void releaseLogs() {
        logger.detachAppender(appender);
    }

    private List<ILoggingEvent> events(Level level) {
        return appender.list.stream().filter(e -> e.getLevel() == level).toList();
    }

    @Test
    public void testX48ASuccessfulRegistrationIsInfo() {
        new RegistrationAuditService(new RegistrationStore()).register("magnus");
        List<ILoggingEvent> info = events(Level.INFO);
        assertEquals(1, info.size(), "One INFO message was expected");
        assertEquals("User magnus registered", info.get(0).getFormattedMessage());
    }

    @Test
    public void testX48AnInvalidUsernameIsAWarning() {
        RegistrationAuditService service = new RegistrationAuditService(new RegistrationStore());
        assertThrows(IllegalArgumentException.class, () -> service.register("  "));
        List<ILoggingEvent> warn = events(Level.WARN);
        assertEquals(1, warn.size(), "One WARN message was expected");
        assertEquals("Rejected registration: invalid username '  '", warn.get(0).getFormattedMessage());
        assertThrows(IllegalArgumentException.class, () -> service.register(null));
        assertEquals("Rejected registration: invalid username 'null'", events(Level.WARN).get(1).getFormattedMessage());
    }

    @Test
    public void testX48AFailureIsAnErrorWithTheException() {
        RegistrationStore failing = new RegistrationStore() {
            @Override
            public void save(String username) {
                throw new IllegalStateException("database down");
            }
        };
        RegistrationAuditService service = new RegistrationAuditService(failing);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> service.register("hikaru"));
        assertEquals("database down", thrown.getMessage(), "The exception must be re-thrown");
        List<ILoggingEvent> error = events(Level.ERROR);
        assertEquals(1, error.size(), "One ERROR message was expected");
        assertEquals("Registration failed for hikaru", error.get(0).getFormattedMessage());
        assertNotNull(error.get(0).getThrowableProxy(), "The ERROR message must include the exception");
        assertEquals("database down", error.get(0).getThrowableProxy().getMessage());
        assertTrue(events(Level.INFO).isEmpty(), "A failed registration must not be logged as registered");
    }

}
