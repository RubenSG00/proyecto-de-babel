package es.us.dp1.chess.tournament.practice.x.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class PlayerCardService {

    private final UserService userService;

    @Autowired
    public PlayerCardService(UserService userService) {
        this.userService = userService;
    }

    // The user belongs to another module: it is obtained through its service, not its repository.
    // findUser already throws ResourceNotFoundException (404).
    @Transactional(readOnly = true)
    public PlayerCard getCard(Integer userId) {
        User user = userService.findUser(userId);
        return new PlayerCard(user.getUsername(), user.getRating(), user.getAuthority().getAuthority());
    }

}
