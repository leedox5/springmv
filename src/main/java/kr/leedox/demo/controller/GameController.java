package kr.leedox.demo.controller;

import kr.leedox.demo.repository.GameRepository;
import kr.leedox.demo.repository.MatchRepository;
import kr.leedox.demo.repository.PlayerRepository;
import kr.leedox.demo.entity.Game;
import kr.leedox.demo.entity.Match;
import kr.leedox.demo.entity.Player;
import kr.leedox.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class GameController {

    @Autowired
    GameRepository repo;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    GameService gameService;

    @GetMapping("/")
    public String gameList(Model model) {

        for (int i = 1; i <= 5; i++) {
            repo.save(new Game(i, "GAME" + i, "entec"));
        }

        List<Game> gameList = new ArrayList<Game>();
        gameList = repo.findAll();

        model.addAttribute("list", gameList);
        return "list";
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable Integer id, Model model) {
        Optional<Game> optionalGame = gameService.findById(id);
        Game game = null;
        if(optionalGame.isPresent()) {
            game = optionalGame.get();
        }
        model.addAttribute("game", game);
        return "detail";
    }

    @GetMapping("/create")
    public String create() {
        return "game_form";
    }

    @PostMapping("/create")
    public RedirectView createGame(Game game) {
        repo.save(game);
        return new RedirectView("/");
    }

    @PostMapping("/player/create/{game_id}")
    public RedirectView playerCreate(@PathVariable Integer game_id, Player player) {
        Game game = repo.getById(game_id);
        player.setGame(game);
        player.setSeq(game.getPlayers().size() + 1);
        playerRepository.save(player);
        return new RedirectView("/game/" + game_id);
    }

    @GetMapping("/match/create/{game_id}")
    public RedirectView matchCreate(@PathVariable Integer game_id) {
        Game game = repo.getById(game_id);
        List<Player> players = (List<Player>) game.getPlayers();

        addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
        addMatch(game, 2, players.get(0), players.get(2), players.get(1), players.get(3));
        addMatch(game, 3, players.get(0), players.get(3), players.get(1), players.get(2));

        return new RedirectView("/game/" + game_id);
    }

    private void addMatch(Game game, int seq, Player p1,  Player p2, Player p3, Player p4) {
        String desc = "";
        Match match = new Match();
        match.setGame(game);
        match.setSeq(seq);
        match.setPlayer1(p1);
        match.setPlayer2(p2);
        match.setPlayer3(p3);
        match.setPlayer4(p4);
        desc += Integer.toString(p1.getSeq()) + p1.getName() + "," + Integer.toString(p2.getSeq()) + p2.getName();
        desc += ":";
        desc += Integer.toString(p3.getSeq()) + p3.getName() + "," + Integer.toString(p4.getSeq()) + p4.getName();
        match.setDescription(desc);
        matchRepository.save(match);
    }

    @PostMapping("/details")
    public String details(@RequestParam("subject")int subject, ModelMap modelMap) {
        modelMap.put("subject", subject);
        return "detail";
    }
}
