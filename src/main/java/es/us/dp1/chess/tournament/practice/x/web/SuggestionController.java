package es.us.dp1.chess.tournament.practice.x.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.chess.tournament.user.UserService;
import jakarta.validation.Valid;

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

    // @Valid: the body is validated before the method runs (400 if not valid). A new object is
    // created from the body without id, author and createdAt, which the client must not decide.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Suggestion> create(@RequestBody @Valid Suggestion suggestion) {
        Suggestion newSuggestion = new Suggestion();
        BeanUtils.copyProperties(suggestion, newSuggestion, "id", "author", "createdAt");
        newSuggestion.setAuthor(userService.findCurrentUser());
        newSuggestion.setCreatedAt(LocalDateTime.now());
        return new ResponseEntity<>(service.save(newSuggestion), HttpStatus.CREATED);
    }

}
