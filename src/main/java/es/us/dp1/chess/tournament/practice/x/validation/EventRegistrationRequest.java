package es.us.dp1.chess.tournament.practice.x.validation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRegistrationRequest {

    String nickname;

    String displayName;

    List<String> interests;

    List<String> languages;

    Integer seats;

    double maxPrice;

}
