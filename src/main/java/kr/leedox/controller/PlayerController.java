package kr.leedox.controller;

import kr.leedox.entity.Game;
import kr.leedox.entity.Player;
import kr.leedox.service.GameService;
import kr.leedox.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    GameService gameService;

    @GetMapping("/player/delete/{game_id}/{player_id}")
    public RedirectView playerCreate(@PathVariable Integer game_id, @PathVariable Integer player_id) {
        playerService.delete(player_id);
        return new RedirectView("/game/" + game_id);
    }

    @GetMapping("/player/ranking/{game_id}")
    public RedirectView playerRanking(@PathVariable Integer game_id) {
        Game game = gameService.getById(game_id);
        playerService.saveRank((List<Player>) game.getPlayers());
        return new RedirectView("/game/" + game_id);
    }
}
