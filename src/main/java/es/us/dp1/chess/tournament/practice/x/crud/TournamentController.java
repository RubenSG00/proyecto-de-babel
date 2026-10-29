package es.us.dp1.chess.tournament.practice.x.crud;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService service;

    @Autowired
    public TournamentController(TournamentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Tournament> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tournament> create(@RequestBody @Valid Tournament tournament) {
        Tournament newTournament = new Tournament();
        BeanUtils.copyProperties(tournament, newTournament, "id");
        return new ResponseEntity<>(service.save(newTournament), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Tournament> update(@PathVariable("id") Integer id, @RequestBody @Valid Tournament tournament) {
        return new ResponseEntity<>(service.update(id, tournament), HttpStatus.OK);
    }

    // 204 No Content: the deletion succeeded and there is no body to return
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
