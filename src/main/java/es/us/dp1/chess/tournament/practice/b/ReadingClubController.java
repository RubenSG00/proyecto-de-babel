package es.us.dp1.chess.tournament.practice.b;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/library/clubs")
public class ReadingClubController {

    private final ReadingClubService service;

    @Autowired
    public ReadingClubController(ReadingClubService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReadingClub>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReadingClub> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    // TODO: Implement to solve Test B7 (GET {id}/overdue-report)

}
