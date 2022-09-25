package kr.leedox.controller;

import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.Game;
import kr.leedox.entity.Player;
import kr.leedox.repository.GameRepository;
import kr.leedox.service.PlayerService;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerRestController {

    @Autowired
    PlayerService playerService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;

    @PostMapping("/club/player/create1/{game_id}")
    public ResponseEntity<?> createPlayer( @PathVariable Integer game_id, @Valid @RequestBody Player player, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        Game game = gameRepository.getById(game_id);
        player.setGame(game);
        player.setSeq(game.getPlayers().size() + 1);
        playerService.save(player);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @GetMapping("/club/player/delete/{player_id}")
    public ResponseEntity<?> playerCreate(@PathVariable Integer player_id) {
        playerService.delete(player_id);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @PostMapping("/wordbook/savemeaning1/{wordbook_id}")
    public ResponseEntity<?> saveMeaning( @PathVariable Integer wordbook_id, @Valid @RequestBody WordMeaning wordMeaning, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        Wordbook wordbookRepo = wordService.getWordbook(wordbook_id);
        wordMeaningService.save(wordbookRepo, wordMeaning);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @GetMapping("/meaning/delete/{id}")
    public ResponseEntity<?> saveMeaning( @PathVariable Integer id) {
        WordMeaning wordMeaning = wordMeaningService.getWordbook(id);
        wordMeaningService.delete(wordMeaning);

		return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }
}
