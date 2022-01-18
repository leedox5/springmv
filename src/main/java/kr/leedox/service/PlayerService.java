package kr.leedox.service;

import kr.leedox.repository.PlayerRepository;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public void save(Player player) {
        player.setName(player.getName().trim());
        playerRepository.save(player);
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

    private int getMatchWin(int win, int lose) {
        if( win > lose) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getMatchLose(int win, int lose) {
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

        int gameWin = player.getScore01() + player.getScore02();
        int gameLose = player.getScore11() + player.getScore12();

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

        int gameWin = player.getScore01() + player.getScore02() + player.getScore03();
        int gameLose = player.getScore11() + player.getScore12() + player.getScore13() ;

        player.setMatchWin(matchWin);
        player.setMatchLose(matchLose);
        player.setGameWin(gameWin);
        player.setGameLose(gameLose);
        player.setGameSum(gameWin - gameLose);
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

        int gameWin = player.getScore01() + player.getScore02() + player.getScore03() + player.getScore04();
        int gameLose = player.getScore11() + player.getScore12() + player.getScore13() + player.getScore14() ;

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
        player.setScore01(0);
        player.setScore11(0);
        player.setScore02(0);
        player.setScore12(0);
        player.setScore03(0);
        player.setScore13(0);
        player.setScore04(0);
        player.setScore14(0);
        player.setMatchWin(0);
        player.setMatchLose(0);
        player.setGameWin(0);
        player.setGameLose(0);
        player.setGameSum(0);
        player.setMatchRank(0);
        playerRepository.save(player);
    }

    public void getRank1(List<Player> players) {
        List<Player> sortedPlayers = new ArrayList<>(players);

        Collections.sort(sortedPlayers, Comparator.comparing(Player::getMatchWin)
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
}
