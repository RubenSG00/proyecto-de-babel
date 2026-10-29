package es.us.dp1.chess.tournament.practice.a;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gym/programs")
public class TrainingProgramController {

    private final TrainingProgramService service;

    @Autowired
    public TrainingProgramController(TrainingProgramService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgram>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TrainingProgram> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    // No try/catch: ExceptionHandlerController turns the service exceptions into 404/403
    @GetMapping("{id}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getLeaderboard(id), HttpStatus.OK);
    }

}
