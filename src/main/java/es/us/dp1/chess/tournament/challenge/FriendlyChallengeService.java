package es.us.dp1.chess.tournament.challenge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
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

    // The access check is application logic, so it lives here and not in the controller
    @Transactional(readOnly = true)
    public List<RankingEntry> getRanking(Integer challengeId) {
        FriendlyChallenge challenge = findById(challengeId);
        User current = userService.findCurrentUser();
        // User does not override equals: compare ids, not instances
        boolean participant = challenge.getParticipants().stream()
            .anyMatch(p -> p.getId().equals(current.getId()));
        if (!participant)
            throw new AccessDeniedException("Only the participants of the challenge can see its ranking");
        List<Object[]> points = repo.findParticipantPoints(challengeId);
        if (points.isEmpty())
            throw new ResourceNotFoundException("No match of the challenge " + challengeId + " has finished yet");
        return points.stream()
            .map(row -> new RankingEntry((User) row[0], ((Number) row[1]).longValue()))
            .toList();
    }

}
