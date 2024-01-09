package kr.leedox.service;

import kr.leedox.entity.Game;
import kr.leedox.repository.GameRepository;
import kr.leedox.repository.PlayerRepository;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    public void save(Player player) {
        player.setName(player.getName().trim());
        playerRepository.save(player);
    }

    public void save(Integer gameId, Player player) {
        Game game = gameRepository.getById(gameId);
        player.setGame(game);
        player.setSeq(game.getPlayers().size() + 1);
        //player.setMatchWin(0);
        //player.setGameSum(0);
        save(player);
    }

    public void clearBySeq(List<Player> players, int seq) {
        for(Player p : players) {
            if(p.getSeq().equals(seq)) {
                delete(p.getId());
            }
        }
    }

    public void saveRankA1(Game game, String name, int seq) {
        deletePlayers(game, seq);
        Player player = Player.builder()
                .name(name)
                .game(game)
                .seq(seq)
                .build();
        save(player);
    }

    public boolean chkName(Integer game_id, Player player) {
        Game game = gameRepository.getById(game_id);

        List<Player> players = (List<Player>) game.getPlayers();

        for (Player p : players) {
            if(p.getName().equals(player.getName().trim())) {
                return false;
            }
        }

        return true;
    }

    public void getRank(List<Player> players) {
        int currRank = 0;
        for ( int i = 0 ; i < players.size() ; i++) {
            currRank = 1 ;
            for (int j = 0; j < players.size(); j++) {
                if (players.get(i).getMatchWin() < players.get(j).getMatchWin()) {
                    currRank++;
                }
            }
            players.get(i).setMatchRank(currRank);
        }

        for ( int i = 0 ; i < players.size() ; i++) {
            currRank = players.get(i).getMatchRank() ;
            for (int j = 0; j < players.size(); j++) {
                if (players.get(i).getMatchRank() == players.get(j).getMatchRank()) {
                    if (players.get(i).getGameSum() < players.get(j).getGameSum()) {
                        currRank++;
                    }
                }
            }
            players.get(i).setMatchRank(currRank);
        }
    }

    public void saveRank(List<Player> players) {
        getRank1(players);
        getRankBirth(players);
        for ( Player p : players ) {
            playerRepository.save(p);
        }
    }

    public void saveScore(Match match, int score1, int score2) {
        saveScore(match.getPlayer1(), score1, score2, match.getMatchSeq1());
        saveScore(match.getPlayer2(), score1, score2, match.getMatchSeq2());
        saveScore(match.getPlayer3(), score2, score1, match.getMatchSeq3());
        saveScore(match.getPlayer4(), score2, score1, match.getMatchSeq4());
    }

    private void saveScore(Player player, int win, int lose, int matchSeq) {
        System.out.println("saveScore--> " + player.getName() + " " + matchSeq);
        if( matchSeq == 1) {
            setScore1(player, win, lose);
        } else if (matchSeq == 2) {
            setScore2(player, win, lose);
        } else if (matchSeq == 3) {
            setScore3(player, win, lose);
        } else if (matchSeq == 4) {
            setScore4(player, win, lose);
        }
        playerRepository.save(player);
    }

    private void setScore1(Player player, int win, int lose) {
        player.setScore01(win);
        player.setScore11(lose);
        player.setMatchWin(win > lose ? 1 : 0);
        player.setMatchLose(win < lose ? 1 : 0);
        player.setGameWin(win);
        player.setGameLose(lose);
        player.setGameSum(win - lose);
    }

    private Integer getMatchWin(Integer win, Integer lose) {
        if(win == null) {
            return 0;
        }

        if( win > lose) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getMatchLose(Integer win, Integer lose) {
        if(win == null) {
            return 0;
        }

        if( win < lose) {
            return 1;
        } else {
            return 0;
        }
    }

    private void setScore2(Player player, int win, int lose) {
        player.setScore02(win);
        player.setScore12(lose);

        int matchWin = 0;
        int matchLose = 0;

        matchWin += getMatchWin(player.getScore01(), player.getScore11());
        matchWin += getMatchWin(player.getScore02(), player.getScore12());

        matchLose += getMatchLose(player.getScore01(), player.getScore11());
        matchLose += getMatchLose(player.getScore02(), player.getScore12());

        //int gameWin = player.getScore01() + player.getScore02();
        //int gameLose = player.getScore11() + player.getScore12();
        Integer gameWin = calcGameWin(player);
        Integer gameLose = calcGameLose(player);

        player.setMatchWin(matchWin);
        player.setMatchLose(matchLose);
        player.setGameWin(gameWin);
        player.setGameLose(gameLose);
        player.setGameSum(gameWin - gameLose);

    }

    private void setScore3(Player player, int win, int lose) {
        player.setScore03(win);
        player.setScore13(lose);

        int matchWin = 0;
        int matchLose = 0;

        matchWin += getMatchWin(player.getScore01(), player.getScore11());
        matchWin += getMatchWin(player.getScore02(), player.getScore12());
        matchWin += getMatchWin(player.getScore03(), player.getScore13());

        matchLose += getMatchLose(player.getScore01(), player.getScore11());
        matchLose += getMatchLose(player.getScore02(), player.getScore12());
        matchLose += getMatchLose(player.getScore03(), player.getScore13());

        //int gameWin = player.getScore01() + player.getScore02() + player.getScore03();
        //int gameLose = player.getScore11() + player.getScore12() + player.getScore13() ;
        Integer gameWin = calcGameWin(player);
        Integer gameLose = calcGameLose(player);

        player.setMatchWin(matchWin);
        player.setMatchLose(matchLose);
        player.setGameWin(gameWin);
        player.setGameLose(gameLose);
        player.setGameSum(gameWin - gameLose);
    }

    private Integer calcGameWin(Player player) {
        Integer sum = 0;
        sum += player.getScore01() == null ? 0 : player.getScore01();
        sum += player.getScore02() == null ? 0 : player.getScore02();
        sum += player.getScore03() == null ? 0 : player.getScore03();
        sum += player.getScore04() == null ? 0 : player.getScore04();
        return sum;
    }

    private Integer calcGameLose(Player player) {
        Integer sum = 0;
        sum += player.getScore11() == null ? 0 : player.getScore11();
        sum += player.getScore12() == null ? 0 : player.getScore12();
        sum += player.getScore13() == null ? 0 : player.getScore13();
        sum += player.getScore14() == null ? 0 : player.getScore14();
        return sum;
    }

    private void setScore4(Player player, int win, int lose) {
        player.setScore04(win);
        player.setScore14(lose);

        int matchWin = 0;
        int matchLose = 0;

        matchWin += getMatchWin(player.getScore01(), player.getScore11());
        matchWin += getMatchWin(player.getScore02(), player.getScore12());
        matchWin += getMatchWin(player.getScore03(), player.getScore13());
        matchWin += getMatchWin(player.getScore04(), player.getScore14());

        matchLose += getMatchLose(player.getScore01(), player.getScore11());
        matchLose += getMatchLose(player.getScore02(), player.getScore12());
        matchLose += getMatchLose(player.getScore03(), player.getScore13());
        matchLose += getMatchLose(player.getScore04(), player.getScore14());

        //int gameWin = player.getScore01() + player.getScore02() + player.getScore03() + player.getScore04();
        //int gameLose = player.getScore11() + player.getScore12() + player.getScore13() + player.getScore14() ;
        Integer gameWin = calcGameWin(player);
        Integer gameLose = calcGameLose(player);

        player.setMatchWin(matchWin);
        player.setMatchLose(matchLose);
        player.setGameWin(gameWin);
        player.setGameLose(gameLose);
        player.setGameSum(gameWin - gameLose);

    }

    public void delete(Integer player_id) {
        playerRepository.deleteById(player_id);
    }

    public void resetResult(Player player) {
        player.setScore01(null);
        player.setScore11(null);
        player.setScore02(null);
        player.setScore12(null);
        player.setScore03(null);
        player.setScore13(null);
        player.setScore04(null);
        player.setScore14(null);
        player.setMatchWin(null);
        player.setMatchLose(null);
        player.setGameWin(null);
        player.setGameLose(null);
        player.setGameSum(null);
        player.setMatchRank(null);
        playerRepository.save(player);
    }

    public void getRank1(List<Player> players) {
        List<Player> sortedPlayers = new ArrayList<>(players);

        sortedPlayers.sort(Comparator.comparing(Player::getMatchWin)
                .thenComparing(Player::getGameSum).reversed());

        for(int i = 0 ; i < sortedPlayers.size() ; i++) {
            int idx = players.indexOf(sortedPlayers.get(i));
            if(i == 0) {
                players.get(idx).setMatchRank(i + 1);
            } else {
                if( sortedPlayers.get(i).getMatchWin() == sortedPlayers.get(i-1).getMatchWin() &&
                    sortedPlayers.get(i).getGameSum() == sortedPlayers.get(i-1).getGameSum()) {
                    players.get(idx).setMatchRank(i);
                } else {
                    players.get(idx).setMatchRank(i + 1);
                }
            }
        }
    }

    public Player findById(Integer id) {
        return playerRepository.findById(id).orElse(null);
    }

    public void getRankBirth(List<Player> players) {
        for(int i = 0 ; i < players.size() ; i++) {
            int r = getNewRankByBirth(players, players.get(i));
            players.get(i).setMatchRank(r);
        }
    }

    private int getNewRankByBirth(List<Player> players, Player curr) {
        for(Player p : players) {
            if(p.getId().equals(curr.getId())) return curr.getMatchRank();
            if(curr.getMatchRank().equals(p.getMatchRank())) {
                try {
                    if(Integer.parseInt(curr.getBirth()) > Integer.parseInt(p.getBirth())) {
                        return curr.getMatchRank() + 1;
                    }
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return curr.getMatchRank();
                }
            }
        }
        return curr.getMatchRank();
    }

    public Game getFinal(int i) {
        Game game = gameRepository.getById(i);
        return game;
    }

    public void saveRankA4(Game game, Player player) {
        for(Player p : game.getPlayers()) {
            if(p.getSeq().equals(4)) {
                playerRepository.delete(p);
            }
        }
        player.setGame(game);
        player.setSeq(4);
        save(player);
    }
    public void saveRankB3(Game game, Player player) {
        for(Player p : game.getPlayers()) {
            if(p.getSeq().equals(3)) {
                playerRepository.delete(p);
            }
        }
        player.setGame(game);
        player.setSeq(3);
        save(player);
    }

    public void saveRankB2(Game game, Player player) {
        for(Player p : game.getPlayers()) {
            if(p.getSeq().equals(2)) {
                playerRepository.delete(p);
            }
        }
        player.setGame(game);
        player.setSeq(2);
        save(player);
    }

    public void deletByGame(Game game) {
        playerRepository.deleteByGame(game);
    }

    public void deletePlayers(Game game, int seq) {
        playerRepository.deletePlayers(game, seq);
    }
}
