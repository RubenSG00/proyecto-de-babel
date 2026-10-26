package es.us.dp1.chess.tournament.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class InvalidWinnerException extends Exception {

	private static final long serialVersionUID = -3906338266891937100L;

	public InvalidWinnerException() {
		super("The winner must be one of the two players of the match.");
	}

	public InvalidWinnerException(final String message) {
		super(message);
	}

}
