package es.us.dp1.chess.tournament.practice.x.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.chess.tournament.user.UserService;

@Service
public class PlayerCardService {

    private final UserService userService;

    @Autowired
    public PlayerCardService(UserService userService) {
        this.userService = userService;
    }

    public PlayerCard getCard(Integer userId) {
        // TODO: Implement to solve Test X25
        return null;
    }

}
