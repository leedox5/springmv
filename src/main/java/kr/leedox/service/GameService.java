package kr.leedox.service;

import kr.leedox.entity.Game;
import kr.leedox.entity.Member;
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
        return gameRepository.findById(gameId);
    }

    public List<Game> getList() {
        return gameRepository.findAllByOrderByGameDateDesc();
    }

    public Game getById(Integer gameId) {
        return gameRepository.getById(gameId);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

	public List<Game> findBySubject(String subject) {
		return gameRepository.findBySubject(subject);
	}

    public Page<Game> getList(int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return this.gameRepository.findAllByOrderByGameDateDesc(pageable);
    }

    public boolean chkPlayer(Integer id) {
        Game game = getById(id);
        return game.getPlayers().isEmpty();
    }

    public Game getThirdPlace(Game game, Member member) {
        String subject = String.format("%s 3,4위전", game.getSubject().substring(0, 12));
        List<Game> games = findBySubject(subject);
        if(games.isEmpty()) {
            return save(new Game(subject, member));
        } else {
            return games.get(0);
        }
    }

    public Game getFinal(Game game, Member member) {
        String subject = String.format("%s 결승", game.getSubject().substring(0, 12));
        List<Game> games = findBySubject(subject);
        if(games.isEmpty()) {
            return save(new Game(subject, member));
        } else {
            return games.get(0);
        }
    }
}
