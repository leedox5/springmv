package kr.leedox.service;

import kr.leedox.entity.Game;
import kr.leedox.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    public Optional<Game> findById(Integer gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        return game;
    }

    public List<Game> getList() {
        return gameRepository.findAllByOrderByGameDateDesc();
    }

    public Game getById(Integer gameId) {
        return gameRepository.getById(gameId);
    }

    public void save(Game game) {
        gameRepository.save(game);
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

	public List<Game> findBySubject(String subject) {
		return gameRepository.findBySubject(subject);
	}

    public Page<Game> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.gameRepository.findAllByOrderByGameDateDesc(pageable);
    }
}
