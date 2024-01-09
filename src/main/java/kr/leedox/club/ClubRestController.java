package kr.leedox.club;

import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.Game;
import kr.leedox.entity.Member;
import kr.leedox.entity.Player;
import kr.leedox.entity.UserCreateForm;
import kr.leedox.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/club")
public class ClubRestController {

    @Autowired
    PlayerService playerService;

    @Autowired
    MatchService matchService;

    @Autowired
    GameService gameService;

    @Autowired
    MemberService memberService;

    @PostMapping("/player/create1/{game_id}")
    public ResponseEntity<?> createPlayer(@PathVariable Integer game_id, @Valid @RequestBody Player player, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        playerService.save(game_id, player);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }


    @PostMapping("/player/savebirth")
    public ResponseEntity<?> saveBirth(@Valid @RequestBody Player player) {
        Player playerRepo = playerService.findById(player.getId());
        playerRepo.setBirth(player.getBirth());
        playerService.save(playerRepo);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @PostMapping("/player/create3")
    public ResponseEntity<?> create3(@RequestBody Game game) {
        Map<String, String> result = new HashMap<>();

        Game gameRepo = gameService.findById(game.getId()).orElse(null);
        String name1 = String.format("%s 결승", gameRepo.getSubject().substring(0, 12));
        String name2 = String.format("%s 3,4위전", gameRepo.getSubject().substring(0, 12));

        List<Game> games1 = gameService.findBySubject(name1);
        List<Game> games2 = gameService.findBySubject(name2);

        List<Player> players = (List<Player>) gameRepo.getPlayers();

        Game gameFinal = games1.get(0);
        Game gameFinal34 = games2.get(0);

        if(gameRepo.getSubject().contains("A")) {
            for(Player p : players) {
                if(p.getMatchRank().equals(1)) {
                    if(chkSeq(gameFinal, 1)) {
                        playerService.saveRankA1(gameFinal, p.getName(), 1);
                    }
                }
                if(p.getMatchRank().equals(2)) {
                    if(chkSeq(gameFinal, 4)) {
                        playerService.saveRankA1(gameFinal, p.getName(), 4);
                    }
                }
                if(p.getMatchRank().equals(3)) {
                    if(chkSeq(gameFinal34, 1)) {
                        playerService.saveRankA1(gameFinal34, p.getName(), 1);
                    }
                }
                if(p.getMatchRank().equals(4)) {
                    if(chkSeq(gameFinal34, 4)) {
                        playerService.saveRankA1(gameFinal34, p.getName(), 4);
                    }
                }
            }
        } else {
            for(Player p : players) {
                if(p.getMatchRank().equals(1)) {
                    if(chkSeq(gameFinal, 3)) {
                        playerService.saveRankA1(gameFinal, p.getName(), 3);
                    }
                }
                if(p.getMatchRank().equals(2)) {
                    if(chkSeq(gameFinal, 2)) {
                        playerService.saveRankA1(gameFinal, p.getName(), 2);
                    }
                }
                if(p.getMatchRank().equals(3)) {
                    if(chkSeq(gameFinal34, 3)) {
                        playerService.saveRankA1(gameFinal34, p.getName(), 3);
                    }
                }
                if(p.getMatchRank().equals(4)) {
                    if(chkSeq(gameFinal34, 2)) {
                        playerService.saveRankA1(gameFinal34, p.getName(), 2);
                    }
                }
            }
        }

        result.put("code", "Y");
        result.put("message", "OK");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private boolean chkSeq(Game game, int seq) {
        List<Player> players = (List<Player>) game.getPlayers();
        for(Player p : players) {
            if(p.getSeq().equals(seq)) {
                if(p.getMatchRank() == null) {
                    return true;
                }
                if(p.getMatchRank() > 0) {
                    return false;
                }
            }
        }
        return true;
    }
    @PostMapping("/player/create2/{game_id}")
    public ResponseEntity<?> create2(@PathVariable Integer game_id, @Valid @RequestBody Player player, Errors errors) {
        HashMap<String, String> result = new HashMap<>();

        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            result.put("code", "N");
            result.put("message", msg.get(0));
            return ResponseEntity.ok(result);
        }

        if(!playerService.chkName(game_id, player)) {
            result.put("code", "N");
            result.put("message", "이미 등록된 이름입니다.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        playerService.save(game_id, player);
        result.put("code", "Y");
        result.put("message", "OK");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/score")
    public ResponseEntity<?> getScore(@AuthenticationPrincipal MemberAdapter author) {
        Score score = new Score(author.getMember());
        score.setMatches(matchService.getScoreMatches(author.getMember()));
        score.setScore(matchService.getScore(score.getMatches(), score.getMember().getUsername()));
        return ResponseEntity.ok(score);
    }

    @PostMapping("/account/chkpw/{id}")
    public  ResponseEntity<?> chkpw(@PathVariable Integer id, @Valid @RequestBody Member member) {
        if(!memberService.chkPw(id, member)) {
            return ResponseEntity.ok(new ErrorResponse("200", "N", "암호가 일치하지 않습니다."));
        }
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @PostMapping("/account/chgpw/{id}")
    public ResponseEntity<?>  signup(@PathVariable Integer id, @Valid @RequestBody UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //return ResponseEntity.ok(new ErrorResponse("200", "N", bindingResult.toString()));
            Map<String, String> errorMap = new HashMap<>();
            List<String> msgs = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                //errorMap.put(error.getField(), error.getDefaultMessage());
                msgs.add(error.getDefaultMessage());
            }
            return new ResponseEntity<List<String>>(msgs, HttpStatus.BAD_REQUEST);
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect","2개의 패스워드가 일치하지 않습니다.");

            List<String> msgs = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                msgs.add(error.getDefaultMessage());
            }
            return new ResponseEntity<List<String>>(msgs, HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.getMember(id);
        /* ---
        member.setEmail(userCreateForm.getEmail());
        member.setUsername(userCreateForm.getUsername());
        --- */
        member.setPassword(userCreateForm.getPassword1());

        memberService.save(member);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "패스워드가 변경 되었습니다."));
    }

}
