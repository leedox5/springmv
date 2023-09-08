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

    @GetMapping("/club/player/delete/{player_id}")
    public ResponseEntity<?> playerCreate(@PathVariable Integer player_id) {
        playerService.delete(player_id);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }
}
