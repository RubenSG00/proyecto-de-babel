package es.us.dp1.chess.tournament.practice.x.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerCardController {

    private final PlayerCardService service;

    @Autowired
    public PlayerCardController(PlayerCardService service) {
        this.service = service;
    }

    // TODO: Implement to solve Test X25 (GET {id}/card)

}
