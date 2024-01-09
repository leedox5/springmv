package kr.leedox.club;

import kr.leedox.CalendarUtil;
import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.*;
import kr.leedox.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/club")
public class ClubController {

    @Autowired
    GameService gameService;

    @Autowired
    WordService wordService;

    @Autowired
    PlayerService playerService;

    @Autowired
    MatchService matchService;

    @Autowired
    private MemberService memberService;

    @Autowired
    RoleService roleService;

	@GetMapping("/")
    public String clubHome(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10050");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/club/intro";
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/record")
    public String getRecord(Model model) {
        Member member = new Member();
        //member.setUsername("선수1");
        model.addAttribute("member", member);
        return "thymeleaf/club/record";
    }

    @GetMapping("/meeting_0822")
    public String getMeeting(Model model) {
        List<Game> gameList = gameService.getList();

        model.addAttribute("list", gameList);
        return "thymeleaf/club/meeting";
    }

    @GetMapping("/meeting")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {
        Page<Game> paging = this.gameService.getList(page);
        model.addAttribute("paging", paging);
        return "thymeleaf/club/paging";
    }
    @GetMapping("/meeting/modify/{id}")
    public String modify(@PathVariable Integer id, Model model) {
        Game game = gameService.getById(id);
        model.addAttribute("game", game);
        return "thymeleaf/club/meeting_form";
    }

    @PostMapping("/meeting/modify/{game_id}")
    public String modifyMeeting(@PathVariable Integer game_id, Game gameForm) {
        Game game = gameService.getById(game_id);
        game.setSubject(gameForm.getSubject());
        gameService.save(game);
        return "redirect:/club/meeting/" + game_id;
    }

    /*
    @GetMapping("/meeting/delete/{game_id}")
    public String deleteMeeting(@PathVariable Integer game_id)  {
        Game game = gameService.getById(game_id);
        gameService.delete(game);
        return "redirect:/club/meeting";
    }
    */

    @GetMapping("/meeting/delete/{id}")
    public ResponseEntity<?> saveMeaning(@PathVariable Integer id) {

        if(!gameService.chkPlayer(id)) {
            return ResponseEntity.ok(new ErrorResponse("200", "N", "선수가 등록되어 있습니다."));
        }

        Game game = gameService.getById(id);
        gameService.delete(game);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @GetMapping("/intro")
    public String getIntro(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10050");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/club/intro";
    }

    @RequestMapping("/login")
    public String login() {
        return "thymeleaf/club/login";
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "thymeleaf/club/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "thymeleaf/club/signup";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "thymeleaf/club/signup";
        }
        
		if (memberService.isExistEmail(userCreateForm.getEmail())) {
			bindingResult.rejectValue("email", "duplicateEmail", "이미 사용된 이메일입니다.");
			return "thymeleaf/club/signup";
		}
		 
        Member member = Member.builder()
                              .email(userCreateForm.getEmail())
                              .username(userCreateForm.getUsername())
                              .build();
        member.setPassword(userCreateForm.getPassword1());
        member.setRegDate(CalendarUtil.formatNow("yyyyMMdd HHmmss"));

        memberService.insertMember(member);

        return "redirect:/club/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Game game = new Game();

		String subject = kr.leedox.CalendarUtil.formatNow("[yyyy.MM.dd]") + " 모임 A";

        List<Game> games = gameService.findBySubject(subject);
        if(games.size() > 0) {
			subject =  kr.leedox.CalendarUtil.formatNow("[yyyy.MM.dd]") + " 모임 B";    
        }

        game.setSubject(subject);
        model.addAttribute("game", game);
        return "thymeleaf/club/meeting_form";
    }

    @PostMapping("/create")
    public String createGame(Game game, @AuthenticationPrincipal MemberAdapter author) {
        Game newGame = new Game(game.getSubject(), author.getMember());
        gameService.save(newGame);
        return "redirect:/club/meeting";
    }

    @GetMapping("/meeting/{id}")
    public String game(@PathVariable Integer id, Model model) {
        Optional<Game> optionalGame = gameService.findById(id);
        Game game = null;
        if(optionalGame.isPresent()) {
            game = optionalGame.get();
        }
        model.addAttribute("game", game);
        return "thymeleaf/club/detail";
    }

    @PostMapping("/player/create/{game_id}")
    public RedirectView playerCreate(@PathVariable Integer game_id, @Valid Player player, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
        }
        Game game = gameService.getById(game_id);
        player.setGame(game);
        player.setSeq(game.getPlayers().size() + 1);
        playerService.save(player);
        return new RedirectView("/club/meeting/" + game_id);
    }

    @GetMapping("/player/ranking/{game_id}")
    public RedirectView playerRanking(@PathVariable Integer game_id) {
        Game game = gameService.getById(game_id);
        playerService.saveRank((List<Player>) game.getPlayers());
        return new RedirectView("/club/meeting/" + game_id);
    }

    @GetMapping("/match/create1/{game_id}")
    public RedirectView matchCreateFinal(@PathVariable Integer game_id) {
       Game game = gameService.getById(game_id);
       List<Player> players = (List<Player>) game.getPlayers();

       int cnt = players.size();

       if( cnt != 4) {
           return new RedirectView("/club/meeting/" + game_id);
       }
       addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));

       return new RedirectView("/club/meeting/" + game_id);
    }

    @GetMapping("/match/create/{game_id}")
    public RedirectView matchCreate(@PathVariable Integer game_id) {
        Game game = gameService.getById(game_id);
        List<Player> players = (List<Player>) game.getPlayers();

        int cnt = players.size();

        switch (cnt) {
            case 4 :
                addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
                addMatch(game, 2, players.get(0), players.get(2), players.get(1), players.get(3));
                addMatch(game, 3, players.get(0), players.get(3), players.get(1), players.get(2));
                break;
            case 5 :
                addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
                addMatch(game, 2, players.get(0), players.get(2), players.get(1), players.get(4));
                addMatch(game, 3, players.get(0), players.get(3), players.get(2), players.get(4));
                addMatch(game, 4, players.get(0), players.get(4), players.get(1), players.get(3));
                addMatch(game, 5, players.get(1), players.get(2), players.get(3), players.get(4));
                break;
            case 6 :
                addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
                addMatch(game, 2, players.get(0), players.get(4), players.get(3), players.get(5));
                addMatch(game, 3, players.get(1), players.get(2), players.get(4), players.get(5));
                addMatch(game, 4, players.get(0), players.get(3), players.get(1), players.get(4));
                addMatch(game, 5, players.get(1), players.get(3), players.get(2), players.get(5));
                addMatch(game, 6, players.get(0), players.get(5), players.get(2), players.get(4));
                break;
            case 7 :
                addMatch(game, 1, players.get(0), players.get(1), players.get(2), players.get(3));
                addMatch(game, 2, players.get(4), players.get(5), players.get(0), players.get(6));
                addMatch(game, 3, players.get(2), players.get(4), players.get(1), players.get(3));
                addMatch(game, 4, players.get(0), players.get(3), players.get(5), players.get(6));
                addMatch(game, 5, players.get(1), players.get(2), players.get(4), players.get(6));
                addMatch(game, 6, players.get(0), players.get(5), players.get(1), players.get(4));
                addMatch(game, 7, players.get(3), players.get(5), players.get(2), players.get(6));
                break;
            default:
                System.out.println("Check Players!!!");
        }

        return new RedirectView("/club/meeting/" + game_id);
    }

    private void addMatch(Game game, int seq, Player p1,  Player p2, Player p3, Player p4) {
        String desc = "";
        Match match = new Match();
        match.setGame(game);
        match.setSeq(seq);
        match.setPlayer1(p1);
        match.setPlayer2(p2);
        match.setPlayer3(p3);
        match.setPlayer4(p4);
        desc += Integer.toString(p1.getSeq()) + p1.getName() + "," + Integer.toString(p2.getSeq()) + p2.getName();
        desc += ":";
        desc += Integer.toString(p3.getSeq()) + p3.getName() + "," + Integer.toString(p4.getSeq()) + p4.getName();
        match.setDescription(desc);
        match.setMatchSeq1(matchService.getMatchSeq(p1));
        match.setMatchSeq2(matchService.getMatchSeq(p2));
        match.setMatchSeq3(matchService.getMatchSeq(p3));
        match.setMatchSeq4(matchService.getMatchSeq(p4));
        match.setMatchDate(game.getGameDate());
        matchService.save(match);
    }

    @PostMapping("/match/save/{match_id}")
    public RedirectView matchSave(@PathVariable Integer match_id, Match matchForm) {
        Match match = matchService.getById(match_id);
        match.setScore1(matchForm.getScore1());
        match.setScore2(matchForm.getScore2());
        matchService.save(match);

        playerService.saveScore(match, matchForm.getScore1(), matchForm.getScore2());

        Game game = match.getGame();

        List<Player> players = (List<Player>) game.getPlayers();

        List<Player> actPlayers = players.stream().filter(t -> t.getMatchWin() != null).collect(Collectors.toList());
        playerService.saveRank(actPlayers);

        return new RedirectView("/club/meeting/" + game.getId());
    }

    @GetMapping("/match/delete/{game_id}")
    public RedirectView matchDelete(@PathVariable Integer game_id) {
        matchService.deleteByGameId(game_id);
        Game game = gameService.getById(game_id);

        for(Player player : game.getPlayers()) {
            playerService.resetResult(player);
        }

        return new RedirectView("/club/meeting/" + game_id);
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Member> members = memberService.getAllMember();
        model.addAttribute("list", members);
        return "thymeleaf/club/admin";
    }

    @GetMapping("/member/{id}")
    public String member(@PathVariable Integer id, Model model) {
        Member member = memberService.getMember(id);

        List<Role> userRoles = new ArrayList<>();
        for(Authority authority : member.getAuthorities()) {
            userRoles.add(authority.getRole());
        }
        member.setRoles(userRoles);

        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("member", member);
        model.addAttribute("roles", roles);
        model.addAttribute("userRoles", userRoles);
        return "thymeleaf/club/member";
    }

    @PostMapping("/member/reset/{id}")
    public String reset(@PathVariable Integer id) {
        Member member = memberService.resetPassword(id);

        return "redirect:/club/admin";
    }

    @PostMapping("/member/save/{id}")
    public String save(@ModelAttribute("member") Member member, @PathVariable Integer id) {
        memberService.save(id, member.getUsername(), member.getRoles());

        return "redirect:/club/member/" + id;
    }

    @GetMapping("/account")
    public String account(Principal principal, Model model) {
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("member", member);
        return "thymeleaf/club/account";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/stat")
    public String stat(@AuthenticationPrincipal MemberAdapter author, Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Score score = new Score(author.getMember());
        score.setMatches(matchService.getScoreMatches(author.getMember()));
        score.setScore(matchService.getScore(score.getMatches(), score.getMember().getUsername()));

        Page<Match> stats = this.matchService.getScoreMatches(author.getMember(), page);

        model.addAttribute("score", score);
        model.addAttribute("stats", stats);
        return "thymeleaf/club/stat";
    }
}
