package es.us.dp1.chess.tournament.practice.x.logging;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

// A Spring Boot test, so that the levels of application.properties are applied
@SpringBootTest
public class TestX49 {

    @Autowired
    DiscountCalculator calculator;

    ListAppender<ILoggingEvent> appender;
    Logger logger;

    @BeforeEach
    public void captureLogs() {
        logger = (Logger) LoggerFactory.getLogger(DiscountCalculator.class);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    public void releaseLogs() {
        logger.detachAppender(appender);
    }

    @Test
    public void testX49TheDiscountItself() {
        assertEquals(15, calculator.discountFor(155));
        assertEquals(10, calculator.discountFor(100));
        assertEquals(0, calculator.discountFor(99));
    }

    @Test
    public void testX49OnlyTheDebugMessageIsShown() {
        calculator.discountFor(150);
        List<String> messages = appender.list.stream().map(e -> e.getLevel() + " " + e.getFormattedMessage()).toList();
        assertTrue(messages.contains("DEBUG Computing discount for 150"),
                "The DEBUG message must be shown with the configured level. Shown: " + messages);
        assertTrue(messages.stream().noneMatch(m -> m.startsWith("TRACE")),
                "The TRACE message must not be shown with level DEBUG. Shown: " + messages);
    }

    @Test
    public void testX49TheTraceMessageIsWritten() {
        // Temporarily lower the level to check that the TRACE message exists in the code
        Level configured = logger.getLevel();
        try {
            logger.setLevel(Level.TRACE);
            calculator.discountFor(150);
            calculator.discountFor(20);
            List<String> messages = appender.list.stream().map(e -> e.getLevel() + " " + e.getFormattedMessage()).toList();
            assertTrue(messages.contains("TRACE Discount rule applied: 10%"), "Shown: " + messages);
            assertTrue(messages.contains("TRACE Discount rule applied: none"), "Shown: " + messages);
        } finally {
            logger.setLevel(configured);
        }
    }

    @Test
    public void testX49OnlyThePackageIsChanged() {
        Logger pkg = (Logger) LoggerFactory.getLogger("es.us.dp1.chess.tournament.practice.x.logging");
        assertEquals(Level.DEBUG, pkg.getLevel(), "The level of the package must be configured to DEBUG");
        assertFalse(LoggerFactory.getLogger("es.us.dp1.chess.tournament.practice.x").isDebugEnabled(),
                "The rest of the application must keep its level");
        assertFalse(LoggerFactory.getLogger("org.springframework").isDebugEnabled(),
                "The rest of the application must keep its level");
    }

}
