package kr.leedox.controller;

import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.Game;
import kr.leedox.entity.Player;
import kr.leedox.repository.GameRepository;
import kr.leedox.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerRestController {

    @Autowired
    PlayerService playerService;

    @Autowired
    GameRepository gameRepository;

    @PostMapping("/player/create1/{game_id}")
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


}
