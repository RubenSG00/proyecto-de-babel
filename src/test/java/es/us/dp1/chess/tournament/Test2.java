package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.challenge.AnnouncementResponse;
import es.us.dp1.chess.tournament.challenge.ChallengeAnnouncement;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@DataJpaTest()
public class Test2 extends ReflexiveTest{

    @Autowired(required = false)
    EntityManager em;

    @Test
    public void test2AnnouncementChallengeAnnotation() {
        checkThatFieldIsAnnotatedWith(ChallengeAnnouncement.class, "challenge", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(ChallengeAnnouncement.class, "challenge", NotNull.class);
    }

    @Test
    public void test2AnnouncementRecipientsAnnotation() {
        checkThatFieldIsAnnotatedWith(ChallengeAnnouncement.class, "recipients", ManyToMany.class);
    }

    @Test
    public void test2AnnouncementRecipientsConstraints() {
        ChallengeAnnouncement a = Test1.createValidAnnouncement(em);
        em.persist(a);

        checkThatFieldIsMandatory(a, "recipients", List.class, em);

        // Se comprueba directamente con el setter (ya generado por Lombok en la
        // plantilla) en lugar de con checkThatValuesAreNotValid, ya que ese helper
        // genérico no puede resolver por reflexión un setter de tipo List a partir
        // del tipo concreto (ArrayList) del valor inválido.
        a.setRecipients(new ArrayList<>(List.of(em.find(User.class, 4))));
        assertThrows(Exception.class, () -> em.flush(),
            "You are not constraining 'recipients' to a minimum of 2 elements, since a list with only one element was considered valid");
    }

    @Test
    public void test2ResponseAnnouncementAnnotation() {
        checkThatFieldIsAnnotatedWith(AnnouncementResponse.class, "announcement", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(AnnouncementResponse.class, "announcement", NotNull.class);
    }

    @Test
    public void test2ResponseRecipientAnnotation() {
        checkThatFieldIsAnnotatedWith(AnnouncementResponse.class, "recipient", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(AnnouncementResponse.class, "recipient", NotNull.class);
    }

}
