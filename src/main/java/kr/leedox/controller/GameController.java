package kr.leedox.controller;

import kr.leedox.common.ErrorResponse;
import kr.leedox.repository.GameRepository;
import kr.leedox.repository.MatchRepository;
import kr.leedox.entity.Game;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import kr.leedox.service.GameService;
import kr.leedox.service.MatchService;
import kr.leedox.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Autowired
    MatchService matchService;

    @GetMapping("/gamehome")
    public String gameList(Model model) {
        /*
        for (int i = 1; i <= 5; i++) {
            repo.save(new Game("GAME" + i, "entec"));
        }
        */
        List<Game> gameList = gameService.getList();

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
    public String create(Model model) {
        Game game = new Game();
        game.setSubject(kr.leedox.CalendarUtil.formatNow("[yyyy.MM.dd]") + " 모임 A");
        model.addAttribute("game", game);
        return "game_form";
    }

    @PostMapping("/create")
    public RedirectView createGame(Game game) {
        Game newGame = new Game(game.getSubject(), "entec");
        gameRepository.save(newGame);
        return new RedirectView("/");
    }

    @PostMapping("/player/create/{game_id}")
    public RedirectView playerCreate(@PathVariable Integer game_id, @Valid Player player, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
        }
        Game game = gameRepository.getById(game_id);
        player.setGame(game);
        player.setSeq(game.getPlayers().size() + 1);
        playerService.save(player);
        return new RedirectView("/game/" + game_id);
    }

    @PostMapping("/match/save/{match_id}")
    public RedirectView matchSave(@PathVariable Integer match_id, Match matchForm) {
        Match match = matchRepository.getById(match_id);
        match.setScore1(matchForm.getScore1());
        match.setScore2(matchForm.getScore2());
        matchRepository.save(match);

        playerService.saveScore(match, matchForm.getScore1(), matchForm.getScore2());

        Game game = match.getGame();

        List<Player> players = (List<Player>) game.getPlayers();

        /*
        players.get(0).setScore01(matchForm.getScore1());
        players.get(0).setScore11(matchForm.getScore2());
        players.get(0).setMatchWin(matchForm.getScore1() > matchForm.getScore2() ? 1 : 0);
        players.get(0).setMatchLose(matchForm.getScore1() < matchForm.getScore2() ? 1 : 0);
        players.get(0).setGameWin(matchForm.getScore1());
        players.get(0).setGameLose(matchForm.getScore2());
        players.get(0).setGameSum(matchForm.getScore1() - matchForm.getScore2());

        players.get(1).setScore01(matchForm.getScore1());
        players.get(1).setScore11(matchForm.getScore2());
        players.get(1).setMatchWin(matchForm.getScore1() > matchForm.getScore2() ? 1 : 0);
        players.get(1).setMatchLose(matchForm.getScore1() < matchForm.getScore2() ? 1 : 0);
        players.get(1).setGameWin(matchForm.getScore1());
        players.get(1).setGameLose(matchForm.getScore2());
        players.get(1).setGameSum(matchForm.getScore1() - matchForm.getScore2());

        players.get(2).setScore01(matchForm.getScore2());
        players.get(2).setScore11(matchForm.getScore1());
        players.get(2).setMatchWin(matchForm.getScore1() < matchForm.getScore2() ? 1 : 0);
        players.get(2).setMatchLose(matchForm.getScore1() > matchForm.getScore2() ? 1 : 0);
        players.get(2).setGameWin(matchForm.getScore2());
        players.get(2).setGameLose(matchForm.getScore1());
        players.get(2).setGameSum(matchForm.getScore2() - matchForm.getScore1());

        players.get(3).setScore01(matchForm.getScore2());
        players.get(3).setScore11(matchForm.getScore1());
        players.get(3).setMatchWin(matchForm.getScore1() < matchForm.getScore2() ? 1 : 0);
        players.get(3).setMatchLose(matchForm.getScore1() > matchForm.getScore2() ? 1 : 0);
        players.get(3).setGameWin(matchForm.getScore2());
        players.get(3).setGameLose(matchForm.getScore1());
        players.get(3).setGameSum(matchForm.getScore2() - matchForm.getScore1());
        */
        playerService.saveRank(players);

        return new RedirectView("/game/" + game.getId());
    }

    @GetMapping("/match/create/{game_id}")
    public RedirectView matchCreate(@PathVariable Integer game_id) {
        Game game = gameRepository.getById(game_id);
        List<Player> players = (List<Player>) game.getPlayers();

        int cnt = players.size();

        switch (cnt) {
            case 4 :
                addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
                addMatch(game, 2, players.get(0), players.get(2), players.get(1), players.get(3));
                addMatch(game, 3, players.get(0), players.get(3), players.get(1), players.get(2));
                break;
            case 5 :
                addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
                addMatch(game, 2, players.get(0), players.get(2), players.get(1), players.get(4));
                addMatch(game, 3, players.get(0), players.get(3), players.get(2), players.get(4));
                addMatch(game, 4, players.get(0), players.get(4), players.get(1), players.get(3));
                addMatch(game, 5, players.get(1), players.get(2), players.get(3), players.get(4));
                break;
            default:
                System.out.println("Check Players!!!");
        }



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
        match.setMatchSeq1(matchService.getMatchSeq(p1));
        match.setMatchSeq2(matchService.getMatchSeq(p2));
        match.setMatchSeq3(matchService.getMatchSeq(p3));
        match.setMatchSeq4(matchService.getMatchSeq(p4));
        matchRepository.save(match);
    }

    @PostMapping("/details")
    public String details(@RequestParam("subject")int subject, ModelMap modelMap) {
        modelMap.put("subject", subject);
        return "detail";
    }
}
