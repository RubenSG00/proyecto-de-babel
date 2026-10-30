package es.us.dp1.chess.tournament.practice.x.json;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/writers")
public class WriterController {

    private final WriterService service;

    @Autowired
    public WriterController(WriterService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<Writer> findWriter(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findWriter(id), HttpStatus.OK);
    }

    @GetMapping("{id}/novels")
    public ResponseEntity<List<Novel>> findNovelsOfWriter(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findNovelsOfWriter(id), HttpStatus.OK);
    }

}
