package kr.leedox.club;

import kr.leedox.common.ErrorResponse;
import kr.leedox.controller.WordController;
import kr.leedox.entity.Game;
import kr.leedox.service.GameService;
import kr.leedox.service.MatchService;
import kr.leedox.service.MemberAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/club")
public class ClubRestController {

    @Autowired
    MatchService matchService;

    @Autowired
    GameService gameService;

    @PostMapping("/score")
    public ResponseEntity<?> getScore(@AuthenticationPrincipal MemberAdapter author) {
        Score score = new Score(author.getMember());
        score.setMatches(matchService.getScoreMatches(author.getMember()));
        score.setScore(matchService.getScore(score.getMatches(), score.getMember().getUsername()));
        return ResponseEntity.ok(score);
    }

}
