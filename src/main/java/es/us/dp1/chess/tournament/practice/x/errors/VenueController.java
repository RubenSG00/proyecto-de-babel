package es.us.dp1.chess.tournament.practice.x.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.chess.tournament.auth.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/v1/venues")
public class VenueController {

    private final VenueService service;

    @Autowired
    public VenueController(VenueService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<Venue> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("{id}/availability")
    public ResponseEntity<MessageResponse> availability(@PathVariable("id") Integer id, @RequestParam("people") int people) {
        Venue venue = service.checkAvailability(id, people);
        return new ResponseEntity<>(new MessageResponse("Venue " + venue.getName() + " is available for " + people + " people"),
                HttpStatus.OK);
    }

    // TODO: Implement to solve Test X16 (POST)

}
