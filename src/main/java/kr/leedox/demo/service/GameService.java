package kr.leedox.demo.service;

import kr.leedox.demo.entity.Game;
import kr.leedox.demo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Optional<Game> findById(Integer gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        return game;
    }
}
