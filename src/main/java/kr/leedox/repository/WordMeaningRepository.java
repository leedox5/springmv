package kr.leedox.repository;

import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordMeaningRepository extends JpaRepository<WordMeaning, Integer> {
    List<WordMeaning> findByWordbook(Wordbook wordbook);
}