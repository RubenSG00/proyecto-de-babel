package es.us.dp1.chess.tournament.challenge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class FriendlyChallengeService {

    private final FriendlyChallengeRepository repo;
    private final UserService userService;

    @Autowired
    public FriendlyChallengeService(FriendlyChallengeRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<FriendlyChallenge> findAll() {
        return (List<FriendlyChallenge>) repo.findAll();
    }

    @Transactional(readOnly = true)
    public FriendlyChallenge findById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("FriendlyChallenge", "id", id));
    }

    @Transactional
    public FriendlyChallenge save(FriendlyChallenge challenge) {
        return repo.save(challenge);
    }

    @Transactional(readOnly = true)
    public List<RankingEntry> getRanking(Integer challengeId) {
        // TODO: Implement to solve Test 7
        return null;
    }

}
