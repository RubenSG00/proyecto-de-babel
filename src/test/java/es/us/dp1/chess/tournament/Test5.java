package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.chess.tournament.exceptions.InvalidWinnerException;
import es.us.dp1.chess.tournament.match.ChessMatch;
import es.us.dp1.chess.tournament.match.ChessMatchRepository;
import es.us.dp1.chess.tournament.match.ChessMatchService;
import es.us.dp1.chess.tournament.user.User;

@ExtendWith(MockitoExtension.class)
public class Test5 extends ReflexiveTest {
    @Mock
    ChessMatchRepository cmr;

    ChessMatchService cms;

    @BeforeEach
    public void configuration() {
        cms = new ChessMatchService(cmr);
    }

    @Test
    public void test5CheckTransactionalityOfChessMatchService() {
        checkTransactional(ChessMatchService.class, "save", ChessMatch.class);
        checkTransactionalRollback(ChessMatchService.class, "save", Arrays.array(ChessMatch.class),
                InvalidWinnerException.class);
        checkTransactional(ChessMatchService.class, "findAll");
        checkTransactional(ChessMatchService.class, "findById", Integer.class);
    }

    @Test
    public void test5ChessMatchServiceCanGetAllMatches() {
        assertNotNull(cms, "ChessMatchService was not injected by spring");
        when(cmr.findAll()).thenReturn(List.of());
        List<ChessMatch> matches = cms.findAll();
        assertNotNull(matches, "The list of matches found by the ChessMatchService was null");
        // The test fails if the service does not invoke the findAll of the repository:
        verify(cmr).findAll();
    }

    @Test
    public void test5ChessMatchServiceCanGetMatchById() {
        assertNotNull(cms, "ChessMatchService was not injected by spring");
        when(cmr.findById(15)).thenReturn(Optional.of(new ChessMatch()));
        cms.findById(15);
        // The test fails if the service does not invoke the findById of the repository:
        verify(cmr).findById(15);
    }

    @Test
    public void test5ChessMatchServiceCanSaveMatchWithoutWinner() {
        assertNotNull(cms, "ChessMatchService was not injected by spring");
        User white = Test1.createUser("white");
        User black = Test1.createUser("black");
        ChessMatch m = new ChessMatch();
        m.setWhite(white);
        m.setBlack(black);
        when(cmr.save(any(ChessMatch.class))).thenReturn(m);
        try {
            cms.save(m);
        } catch (InvalidWinnerException e) {
            fail("Exception should not be thrown");
        }
        // The test fails if the service does not invoke the save function of the
        // repository:
        verify(cmr).save(m);
    }

    @Test
    public void test5ChessMatchServiceCanSaveMatchWithValidWinner() {
        assertNotNull(cms, "ChessMatchService was not injected by spring");
        User white = Test1.createUser("white");
        User black = Test1.createUser("black");
        ChessMatch m = new ChessMatch();
        m.setWhite(white);
        m.setBlack(black);
        m.setWinner(white);
        when(cmr.save(any(ChessMatch.class))).thenReturn(m);
        try {
            cms.save(m);
        } catch (InvalidWinnerException e) {
            fail("Exception should not be thrown");
        }
        verify(cmr).save(m);
    }

    @Test
    public void test5ChessMatchServiceCanNotSaveMatchWithInvalidWinner() {
        assertNotNull(cms, "ChessMatchService was not injected by spring");
        User white = Test1.createUser("white");
        User black = Test1.createUser("black");
        User someoneElse = Test1.createUser("someoneElse");
        ChessMatch m = new ChessMatch();
        m.setWhite(white);
        m.setBlack(black);
        m.setWinner(someoneElse);
        // The test fails if the service does not throw the exception;
        assertThrows(InvalidWinnerException.class, () -> {
            cms.save(m);
        });
        // The test fails if the service invokes the save function of the repository:
        verify(cmr, never()).save(m);
    }

}
