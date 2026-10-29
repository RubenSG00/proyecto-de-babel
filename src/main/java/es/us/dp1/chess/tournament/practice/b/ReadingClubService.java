package es.us.dp1.chess.tournament.practice.b;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class ReadingClubService {

    private final ReadingClubRepository repo;
    private final UserService userService;

    @Autowired
    public ReadingClubService(ReadingClubRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ReadingClub> findAll() {
        return (List<ReadingClub>) repo.findAll();
    }

    @Transactional(readOnly = true)
    public ReadingClub findById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ReadingClub", "id", id));
    }

    @Transactional(readOnly = true)
    public List<OverdueEntry> getOverdueReport(Integer clubId) {
        // TODO: Implement to solve Test B7
        return null;
    }

}
