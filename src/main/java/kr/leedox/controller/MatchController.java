package kr.leedox.controller;

import kr.leedox.entity.Game;
import kr.leedox.entity.Player;
import kr.leedox.service.GameService;
import kr.leedox.service.MatchService;
import kr.leedox.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller
public class MatchController {

    @Autowired
    MatchService matchService;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerServce;

    @GetMapping("/match/delete/{game_id}")
    public RedirectView matchDelete(@PathVariable Integer game_id) {
        matchService.deleteByGameId(game_id);
        Game game = gameService.getById(game_id);

        for(Player player : game.getPlayers()) {
            playerServce.resetResult(player);
        }

        return new RedirectView("/club/meeting/" + game_id);
    }
}
