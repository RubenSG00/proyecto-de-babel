package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.challenge.AnnouncementResponse;
import es.us.dp1.chess.tournament.challenge.AnnouncementResponseRepository;
import es.us.dp1.chess.tournament.challenge.AnnouncementStatus;
import es.us.dp1.chess.tournament.challenge.ChallengeAnnouncement;
import es.us.dp1.chess.tournament.challenge.ChallengeAnnouncementRepository;
import es.us.dp1.chess.tournament.challenge.FriendlyChallenge;
import es.us.dp1.chess.tournament.challenge.ResponseStatus;
import es.us.dp1.chess.tournament.user.Authorities;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@DataJpaTest()
public class Test1 extends ReflexiveTest {

    @Autowired(required = false)
    ChallengeAnnouncementRepository challengeAnnouncementRepository;

    @Autowired(required = false)
    AnnouncementResponseRepository announcementResponseRepository;

    @Autowired
    EntityManager em;

    @Test
    public void test1RepositoriesExist() {
        assertNotNull(challengeAnnouncementRepository,
                "The ChallengeAnnouncement repository was not injected into the tests, its autowired value was null");
        assertNotNull(announcementResponseRepository,
                "The AnnouncementResponse repository was not injected into the tests, its autowired value was null");

        test1ChallengeAnnouncementRepositoryContainsMethod();
        test1AnnouncementResponseRepositoryContainsMethod();
    }

    public void test1ChallengeAnnouncementRepositoryContainsMethod() {
        if (challengeAnnouncementRepository != null) {
            Object v = challengeAnnouncementRepository.findById(474629);
            assertFalse(null != v && ((Optional) v).isPresent(),
                    "No result (null) should be returned for an announcement that does not exist");
        } else
            fail("The ChallengeAnnouncement repository was not injected into the tests, its autowired value was null");
    }

    public void test1AnnouncementResponseRepositoryContainsMethod() {
        if (announcementResponseRepository != null) {
            Object v = announcementResponseRepository.findById(474629);
            assertFalse(null != v && ((Optional) v).isPresent(),
                    "No result (null) should be returned for a response that does not exist");
        } else
            fail("The AnnouncementResponse repository was not injected into the tests, its autowired value was null");
    }

    @Test
    public void test1CheckAnnouncementConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "message", List.of(
                        "short",
                        "This message is deliberately written to be much longer than the sixty character limit allowed for a challenge announcement, well past it."));

        ChallengeAnnouncement a = createValidAnnouncement(em);
        em.persist(a);
        checkThatEntityIsValid(a, em);

        checkThatFieldsAreMandatory(a, em, "message", "status");

        checkThatValuesAreNotValid(a, invalidValues, em);
    }

    @Test
    public void test1CheckResponseConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "message", List.of(
                        "hi",
                        "This message is deliberately written to be much longer than the two hundred character limit allowed for an announcement response, since it needs to go past two hundred characters in total length to trigger the validation error that we expect to see appear here in this very long sentence."));

        AnnouncementResponse r = createValidResponse(em);
        em.persist(r);
        checkThatEntityIsValid(r, em);

        checkThatFieldsAreMandatory(r, em, "message", "status", "respondedAt");

        checkThatValuesAreNotValid(r, invalidValues, em);
    }

    @Test
    public void test1CheckAnnouncementAnnotations() throws NoSuchFieldException, SecurityException {
        assertTrue(classIsAnnotatedWith(ChallengeAnnouncement.class, Entity.class));
        // 1. Obtenemos el campo status.
        java.lang.reflect.Field statusField = ChallengeAnnouncement.class.getDeclaredField("status");

        // 2. Comprobamos que el Field posee la anotación
        assertTrue(
                statusField.isAnnotationPresent(Enumerated.class),
                "El atributo 'status' carece de la anotación @Enumerated");

        // 3. Inspeccionamos el valor de la anotación
        Enumerated enumerated = statusField.getAnnotation(Enumerated.class);
        assertEquals(
                EnumType.STRING,
                enumerated.value(),
                "La anotación @Enumerated no está configurada con EnumType.STRING");
    }

    @Test
    public void test1CheckResponseAnnotations() throws NoSuchFieldException, SecurityException {
        assertTrue(classIsAnnotatedWith(AnnouncementResponse.class, Entity.class));
        java.lang.reflect.Field statusField = AnnouncementResponse.class.getDeclaredField("status");

        assertTrue(
                statusField.isAnnotationPresent(Enumerated.class),
                "El atributo 'status' carece de la anotación @Enumerated");

        Enumerated enumerated = statusField.getAnnotation(Enumerated.class);
        assertEquals(
                EnumType.STRING,
                enumerated.value(),
                "La anotación @Enumerated no está configurada con EnumType.STRING");
    }

    public static ChallengeAnnouncement createValidAnnouncement(EntityManager em) {
        ChallengeAnnouncement a = new ChallengeAnnouncement();
        a.setMessage("This is a valid announcement message");
        a.setStatus(AnnouncementStatus.DRAFT);
        if (em != null) {
            a.setChallenge(em.find(FriendlyChallenge.class, 1));
            a.setRecipients(new ArrayList<>(List.of(em.find(User.class, 4), em.find(User.class, 5))));
        }
        return a;
    }

    public static AnnouncementResponse createValidResponse(EntityManager em) {
        AnnouncementResponse r = new AnnouncementResponse();
        r.setMessage("This is a valid response");
        r.setStatus(ResponseStatus.ACCEPTED);
        r.setRespondedAt(LocalDateTime.of(2026, 7, 1, 11, 0));
        if (em != null) {
            ChallengeAnnouncement a = createValidAnnouncement(em);
            em.persist(a);
            r.setAnnouncement(a);
            r.setRecipient(em.find(User.class, 4));
        }
        return r;
    }

    public static User createUser(String name) {
        Authorities a1 = new Authorities();
        a1.setAuthority("PANA");
        User u1 = new User();
        setValue(u1, "username", String.class, name);
        setValue(u1, "authority", Authorities.class, a1);
        return u1;
    }

}
