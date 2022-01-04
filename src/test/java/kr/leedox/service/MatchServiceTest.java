package kr.leedox.service;

import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest( properties = {"spring.config.location=classpath:application.properties"} )
@Transactional
class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @Test
    void findByPlayer1Test() {
        Player p = Player.builder().id(9).build();

        List<Match> matches = matchService.findByPlayer1(p);

        assertEquals(4, matches.size());

        for(Match match : matches) {
            System.out.println(match.getDescription() + " " + match.getSeq());
        }
    }

    @Test
    void getMatchSeqTest() {

        Player p = Player.builder().id(9).build();

        assertEquals(1, matchService.getMatchSeq1(p));
    }
}