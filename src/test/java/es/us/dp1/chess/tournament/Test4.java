package es.us.dp1.chess.tournament;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.challenge.ChallengeAnnouncement;
import jakarta.persistence.EntityManager;

@DataJpaTest
public class Test4 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Test
    public void test4Announcement1Recipients() {
        checkContainsById(ChallengeAnnouncement.class,1,"getRecipients",4,em);
        checkContainsById(ChallengeAnnouncement.class,1,"getRecipients",5,em);
    }

    @Test
    public void test4Announcement2Recipients() {
        checkContainsById(ChallengeAnnouncement.class,2,"getRecipients",4,em);
        checkContainsById(ChallengeAnnouncement.class,2,"getRecipients",5,em);
    }

    @Test
    public void test4Announcement3Recipients() {
        checkContainsById(ChallengeAnnouncement.class,3,"getRecipients",5,em);
        checkContainsById(ChallengeAnnouncement.class,3,"getRecipients",6,em);
        checkContainsById(ChallengeAnnouncement.class,3,"getRecipients",7,em);
    }

}
