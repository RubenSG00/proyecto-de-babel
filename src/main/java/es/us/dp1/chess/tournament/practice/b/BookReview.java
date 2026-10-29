package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookReview extends BaseEntity {

    @NotNull
    @Size(min = 20, max = 500)
    String content;

    // @Min/@Max for integer types (@Size is only for strings and collections)
    @NotNull
    @Min(1)
    @Max(5)
    Integer stars;

    @NotNull
    @Enumerated(EnumType.STRING)
    ReviewVisibility visibility;

    // Optional: @Past alone accepts null
    @Past
    LocalDate publishedOn;

    @NotNull
    @ManyToOne(optional = false)
    ReadingClub club;

    @NotNull
    @ManyToOne(optional = false)
    User author;

    // Navigable end "0..3": no @NotNull/@NotEmpty (it may be empty), only the maximum
    @Size(max = 3)
    @ManyToMany
    List<User> mentionedMembers;

}
