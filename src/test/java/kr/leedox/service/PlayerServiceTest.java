package kr.leedox.service;

import kr.leedox.repository.MatchRepository;
import kr.leedox.entity.Game;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import kr.leedox.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest( properties = {"spring.config.location=classpath:application.properties"} )
@Transactional
public class PlayerServiceTest {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Autowired
    MatchRepository matchRepository;

    @Test
    public void getRankTest() {
        Game game = gameRepository.getById(11);
        List<Player> players = (List<Player>) game.getPlayers();

        printPlayers(1, players);

        playerService.getRank(players);

        printPlayers(2, players);
    }

    @Test
    public void getRank1Test() {
        Game game = gameService.getById(10);

        List<Player> players = (List<Player>) game.getPlayers();

        printPlayers(1, players);

        playerService.getRank1(players);

        /*
        Collections.sort(players, Comparator.comparing(Player::getMatchWin)
                        .thenComparing(Player::getGameSum).reversed());
        */



        printPlayers(2, players);

    }

    @Test
    public void getFinalTest() {
        Game game = gameService.findById(10).orElse(null);
        String name = String.format("%s 결승", Objects.requireNonNull(game).getSubject().substring(0, 12));
        assertThat(name).isEqualTo("[2024.01.07] 결승");

        List<Game> games = gameService.findBySubject(name);
        assertThat(games.size()).isEqualTo(1);
        assertThat(games.get(0).getId()).isEqualTo(12);

        List<Player> players = (List<Player>) game.getPlayers();
        printPlayers(1, players);

        for(Player p : players) {
            if(p.getMatchRank().equals(1)) {
                System.out.println(p.getName());
                playerService.save(games.get(0).getId(), p);
            }
        }
    }

    @Test
    public void getRankBirthTest() {
        Game game = gameService.getById(10);
        List<Player> players = (List<Player>) game.getPlayers();

        assertThat(players.size()).isEqualTo(4);

        printPlayers(1, players);
        for(Player p : players) {
            List<Player> list = getEqRank(players, p);
            System.out.printf("%s %d%n", p.getName(), list.size());
        }

        for(int i = 0 ; i < players.size() ; i++) {
            int r = getBirthRank(players, players.get(i));
            players.get(i).setMatchRank(r);
        }

        assertThat(players.get(1).getMatchRank()).isEqualTo(4);
        printPlayers(2, players);
    }

    private int getBirthRank(List<Player> players, Player curr) {
        for(Player p : players) {
            if(p.getId().equals(curr.getId())) return curr.getMatchRank();
            if(curr.getMatchRank().equals(p.getMatchRank())) {
                if(Integer.parseInt(curr.getBirth()) > Integer.parseInt(p.getBirth())) {
                    return curr.getMatchRank() + 1;
                }
            }
        }
        return curr.getMatchRank();
    }

    private List<Player> getEqRank(List<Player> players, Player curr) {
        List<Player> _players = new ArrayList<>();

        for(Player p : players) {
            if(curr.getMatchRank().equals(p.getMatchRank())) {
                _players.add(p);
            }
        }
        return _players;
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
            System.out.println(String.format("%10s %3d %3d %3d %s", p.getName(), p.getMatchWin(), p.getGameSum(), p.getMatchRank(), p.getBirth()));
        }
    }


}
