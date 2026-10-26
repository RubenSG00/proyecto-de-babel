package es.us.dp1.chess.tournament.challenge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/challenges")
public class FriendlyChallengeController {

    private final FriendlyChallengeService service;

    @Autowired
    public FriendlyChallengeController(FriendlyChallengeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FriendlyChallenge>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FriendlyChallenge> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    // TODO: Implement to solve Test 7 (GET {id}/ranking)

}
