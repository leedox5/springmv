package kr.leedox.repository;

import kr.leedox.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findAllByOrderByIdDesc();
	List<Game> findBySubject(String subject);

    List<Game> findAllByOrderByGameDateDesc();
    Page<Game> findAllByOrderByGameDateDesc(Pageable pageable);
}