package kr.leedox.club;

import kr.leedox.common.ErrorResponse;
import kr.leedox.service.MatchService;
import kr.leedox.service.MemberAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClubRestController {

    @Autowired
    MatchService matchService;

    @PostMapping("/club/score")
    public ResponseEntity<?> getScore(@AuthenticationPrincipal MemberAdapter author) {
        Score score = new Score(author.getMember());
        score.setMatches(matchService.getScoreMatches(author.getMember()));
        score.setScore(matchService.getScore(score.getMatches(), score.getMember().getUsername()));
        return ResponseEntity.ok(score);
    }
}
