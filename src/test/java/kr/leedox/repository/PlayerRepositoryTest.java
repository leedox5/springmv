package kr.leedox.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.leedox.entity.Game;
import kr.leedox.entity.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureJson
public class PlayerRepositoryTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @DisplayName("저장 테스트")
    @Test
    public void saveTest() {
        Player player = Player
                .builder()
                .name("테스트")
                .build();
        Player playerRepo = playerRepository.save(player);
        assertThat(playerRepo.getName()).isEqualTo("테스트");
    }

    @DisplayName("삭제 테스트")
    @Test
    public void deleteTest() {
        Player player = playerRepository.getById(36);
        assertThat(player.getSeq()).isEqualTo(1);
        playerRepository.deleteById(player.getId());
    }

    @DisplayName("게임별 삭제 테스트")
    @Test
    public void deleteByGameTest() {
        Player player = playerRepository.getById(38);
        playerRepository.deleteByGame(player.getGame());
    }

    @DisplayName("게임 seq별 삭제 테스트")
    @Test
    public void deletePlayersTest() {
        Game game = gameRepository.getById(12);
        playerRepository.deletePlayers(game, 4);
    }
}