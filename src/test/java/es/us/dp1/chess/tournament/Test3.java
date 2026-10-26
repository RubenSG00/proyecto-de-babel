package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.challenge.AnnouncementResponse;
import es.us.dp1.chess.tournament.challenge.AnnouncementStatus;
import es.us.dp1.chess.tournament.challenge.ChallengeAnnouncement;
import es.us.dp1.chess.tournament.challenge.ResponseStatus;
import jakarta.persistence.EntityManager;

@DataJpaTest()
public class Test3 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Test
    public void test3InitialAnnouncement1(){
        ChallengeAnnouncement a=em.find(ChallengeAnnouncement.class,1);
        assertNotNull(a,"There should exist a ChallengeAnnouncement with id:1");
        assertEquals("Remember to submit your match results by Friday",getFieldValueReflexively(a, "message"));
        assertEquals(AnnouncementStatus.SENT,a.getStatus());
        assertEquals(LocalDateTime.of(2026,7,1,10,0), a.getSentAt());
        checkLinkedById(ChallengeAnnouncement.class,1,"getChallenge",1,em);
    }

    @Test
    public void test3InitialAnnouncement2(){
        ChallengeAnnouncement a=em.find(ChallengeAnnouncement.class,2);
        assertNotNull(a,"There should exist a ChallengeAnnouncement with id:2");
        assertEquals("New format proposal for next season",getFieldValueReflexively(a, "message"));
        assertEquals(AnnouncementStatus.DRAFT,a.getStatus());
        assertNull(a.getSentAt());
        checkLinkedById(ChallengeAnnouncement.class,2,"getChallenge",1,em);
    }

    @Test
    public void test3InitialAnnouncement3(){
        ChallengeAnnouncement a=em.find(ChallengeAnnouncement.class,3);
        assertNotNull(a,"There should exist a ChallengeAnnouncement with id:3");
        assertEquals("Please confirm your availability for August",getFieldValueReflexively(a, "message"));
        assertEquals(AnnouncementStatus.SENT,a.getStatus());
        assertEquals(LocalDateTime.of(2026,8,2,9,30), a.getSentAt());
        checkLinkedById(ChallengeAnnouncement.class,3,"getChallenge",2,em);
    }

    @Test
    public void test3InitialResponse1(){
        AnnouncementResponse r=em.find(AnnouncementResponse.class,1);
        assertNotNull(r,"There should exist an AnnouncementResponse with id:1");
        assertEquals("Got it, will submit by Friday",getFieldValueReflexively(r, "message"));
        assertEquals(ResponseStatus.ACCEPTED,r.getStatus());
        assertEquals(LocalDateTime.of(2026,7,1,11,0), r.getRespondedAt());
        checkLinkedById(AnnouncementResponse.class,1,"getAnnouncement",1,em);
        checkLinkedById(AnnouncementResponse.class,1,"getRecipient",4,em);
    }

    @Test
    public void test3InitialResponse2(){
        AnnouncementResponse r=em.find(AnnouncementResponse.class,2);
        assertNotNull(r,"There should exist an AnnouncementResponse with id:2");
        assertEquals("Sounds good to me",getFieldValueReflexively(r, "message"));
        assertEquals(ResponseStatus.ACCEPTED,r.getStatus());
        assertEquals(LocalDateTime.of(2026,8,2,10,0), r.getRespondedAt());
        checkLinkedById(AnnouncementResponse.class,2,"getAnnouncement",3,em);
        checkLinkedById(AnnouncementResponse.class,2,"getRecipient",5,em);
    }

    @Test
    public void test3InitialResponse3(){
        AnnouncementResponse r=em.find(AnnouncementResponse.class,3);
        assertNotNull(r,"There should exist an AnnouncementResponse with id:3");
        assertEquals("I can't make it in August",getFieldValueReflexively(r, "message"));
        assertEquals(ResponseStatus.DECLINED,r.getStatus());
        assertEquals(LocalDateTime.of(2026,8,2,12,0), r.getRespondedAt());
        checkLinkedById(AnnouncementResponse.class,3,"getAnnouncement",3,em);
        checkLinkedById(AnnouncementResponse.class,3,"getRecipient",6,em);
    }

}
