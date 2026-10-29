package es.us.dp1.chess.tournament.practice.x.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.chess.tournament.user.UserService;

@RestController
@RequestMapping("/api/v1/suggestions")
public class SuggestionController {

    private final SuggestionService service;
    private final UserService userService;

    @Autowired
    public SuggestionController(SuggestionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Suggestion>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    // TODO: Implement to solve Test X11 (POST)

}
