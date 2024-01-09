package kr.leedox.repository;

import kr.leedox.entity.Game;
import kr.leedox.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    void deleteByGame(Game game);

    @Transactional
    @Modifying
    @Query("delete from Player where game = :game and seq = :seq")
    void deletePlayers(@Param("game") Game game, @Param("seq") int seq);
}