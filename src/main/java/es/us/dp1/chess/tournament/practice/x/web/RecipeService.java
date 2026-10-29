package es.us.dp1.chess.tournament.practice.x.web;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class RecipeService {

    private final RecipeRepository repo;
    private final UserService userService;

    @Autowired
    public RecipeService(RecipeRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Recipe findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
    }

    // Mass assignment: never save the received object. Only name and minutes come from the
    // client; a new object (no id: always an INSERT) gets the owner and featured from the server.
    @Transactional
    public Recipe create(Recipe recipe) {
        Recipe newRecipe = new Recipe();
        BeanUtils.copyProperties(recipe, newRecipe, "id", "owner", "featured");
        newRecipe.setOwner(userService.findCurrentUser());
        newRecipe.setFeatured(false);
        return repo.save(newRecipe);
    }

}
