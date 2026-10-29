package es.us.dp1.chess.tournament.practice.x.customvalidation;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomBooking {

    String room;

    LocalDateTime checkIn;

    LocalDateTime checkOut;

    Integer guests;

}
