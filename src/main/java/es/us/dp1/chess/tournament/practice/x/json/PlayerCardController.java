package es.us.dp1.chess.tournament.practice.x.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("{id}/card")
    public ResponseEntity<PlayerCard> getCard(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getCard(id), HttpStatus.OK);
    }

}
