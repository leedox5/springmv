package kr.leedox.service;

import kr.leedox.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceMockTest {
    @InjectMocks
    PlayerService playerService;

    @Test
    public void getRankBirthTest() {
        List<Player> players = getStubPlayers();
        playerService.getRankBirth(players);

        assertThat(players.get(1).getMatchRank()).isEqualTo(4);
    }

    private List<Player> getStubPlayers() {
        Player p1 = Player.builder().id(10).name("박종만").matchRank(3).birth("1975").build();
        Player p2 = Player.builder().id(11).name("강민").matchRank(3).birth("1978").build();
        return List.of(p1, p2);
    }

}
