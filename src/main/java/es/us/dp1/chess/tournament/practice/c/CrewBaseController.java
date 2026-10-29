package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/airline/bases")
public class CrewBaseController {

    private final CrewBaseService service;

    @Autowired
    public CrewBaseController(CrewBaseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CrewBase>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CrewBase> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    // Only HTTP concerns here: the optional request parameter and the status code. No try/catch:
    // ExceptionHandlerController turns the service exceptions into 400/403/404.
    @GetMapping("{id}/pilot-ranking")
    public ResponseEntity<List<PilotScore>> getPilotRanking(@PathVariable("id") Integer id,
            @RequestParam(required = false, defaultValue = "0") Integer minScore) {
        return new ResponseEntity<>(service.getPilotRanking(id, minScore), HttpStatus.OK);
    }

}
