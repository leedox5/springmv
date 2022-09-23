package kr.leedox.service;

import kr.leedox.entity.Game;
import kr.leedox.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return gameRepository.findAllByOrderByIdDesc();
    }

    public Game getById(Integer gameId) {
        return gameRepository.getById(gameId);
    }

    public void save(Game game) {
        gameRepository.save(game);
    }
}
