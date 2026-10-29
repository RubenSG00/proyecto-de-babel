package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;
import java.util.List;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReview {

    String content;

    Integer stars;

    ReviewVisibility visibility;

    LocalDate publishedOn;

    @Transient
    ReadingClub club;

    @Transient
    User author;

    @Transient
    List<User> mentionedMembers;

}
