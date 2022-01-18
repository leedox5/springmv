package kr.leedox.service;

import kr.leedox.entity.Game;
import kr.leedox.repository.MatchRepository;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<Match> findByPlayer1(Player p) {
        return matchRepository.findByPlayer1(p);
    }

    private List<Match> findByPlayer2(Player p) {
        return matchRepository.findByPlayer2(p);
    }

    public int getMatchSeq(Player p) {
        int matchSeq = 1;
        matchSeq += getMatchSeq1(p);
        matchSeq += getMatchSeq2(p);
        matchSeq += getMatchSeq3(p);
        matchSeq += getMatchSeq4(p);
        return matchSeq;
    }

    public int getMatchSeq1(Player p) {
        return findByPlayer1(p).size();
    }

    public int getMatchSeq2(Player p) {
        return findByPlayer2(p).size();
    }

    public int getMatchSeq3(Player p) {
        return matchRepository.findByPlayer3(p).size();
    }

    public int getMatchSeq4(Player p) {
        return matchRepository.findByPlayer4(p).size();
    }

    public void deleteByGameId(Integer game_id) {
        matchRepository.deleteByGameId(game_id);
    }
}
