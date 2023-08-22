package kr.leedox.repository;

import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByPlayer1(Player p);
    List<Match> findByPlayer2(Player p);
    List<Match> findByPlayer3(Player p);
    List<Match> findByPlayer4(Player p);

    @Transactional
    List<Match> deleteByGameId(Integer game_id);

    List<Match> findByDescriptionContains(String username);

    List<Match> findByDescriptionContainsOrderByIdDesc(String username);

    List<Match> findByDescriptionContainsOrderByIdDescSeqAsc(String username);

    List<Match> findByDescriptionContainsOrderByGameIdDescSeqAsc(String username);

    List<Match> findByDescriptionContainsOrderByMatchDateDescSeqAsc(String username);
    Page<Match> findByDescriptionContainsOrderByMatchDateDescSeqAsc(Pageable pageable, String username);
    Page<Match> findByDescriptionContainsAndScore1NotNullOrderByMatchDateDescSeqAsc(Pageable pageable, String username);
}