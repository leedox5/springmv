package kr.leedox.service;

import kr.leedox.demo.repository.MatchRepository;
import kr.leedox.entity.Game;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import kr.leedox.demo.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest( properties = {"spring.config.location=classpath:application.properties"} )
@Transactional
public class PlayerServiceTest {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    MatchRepository matchRepository;

    @Test
    public void getRankTest() {
        Game game = gameRepository.getById(10);
        List<Player> players = (List<Player>) game.getPlayers();

        printPlayers(1, players);

        playerService.getRank(players);

        printPlayers(2, players);
    }

    @Test
    public void saveScoreTest() {
        Match match = matchRepository.getById(71);

        List<Player> players = new ArrayList<>();
        players.add(match.getPlayer1());

        playerService.saveScore(match, 1, 4);

        printPlayers(1, players);

    }

    private void printPlayers(int no, List<Player> players) {
        System.out.println(no + "==================================");
        for (Player p : players) {
            System.out.println(p.getName() + " " + p.getMatchWin() + " " + p.getGameSum() + " " + p.getMatchRank());
        }
    }
}
