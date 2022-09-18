package kr.leedox.repository;

import kr.leedox.entity.WordMeaning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordMeaningRepository extends JpaRepository<WordMeaning, Integer> {
}