package kr.leedox.demo.repository;

import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByPlayer1(Player p);
    List<Match> findByPlayer2(Player p);
    List<Match> findByPlayer3(Player p);
    List<Match> findByPlayer4(Player p);
}