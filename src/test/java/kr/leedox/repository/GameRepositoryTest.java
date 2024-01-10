package kr.leedox.repository;

import kr.leedox.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureJson
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @Test
    public void getByIdTest() {
        Game game = gameRepository.getById(25);
        assertThat(game.getPlayerCount()).isEqualTo(4);
    }

    @Test
    public void deleteTest() {
        Game game = gameRepository.getById(17);
        assertThat(game.getPlayers().size()).isEqualTo(4);

        gameRepository.delete(game);
    }

    @Test
    public void saveTest() {
        Game game = new Game("0110 TEST", "TESTER");
        System.out.println(game);
        Game gameRepo = gameRepository.save(game);
        System.out.println(gameRepo);
        assertThat(gameRepo.getPlayers()).isNotNull();
    }
}