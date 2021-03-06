package kr.leedox.repository;

import kr.leedox.entity.Wordbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface WordRepository extends JpaRepository<Wordbook, Integer> {
    List<Wordbook> findByWord(String word);
    List<Wordbook> findByWordContaining(String word);
    List<Wordbook> findByWordStartsWithOrderByWordAsc(String word);
    List<Wordbook> findTop10ByOrderByUpdDateDesc();

    List<Wordbook> findTop10BySeqGreaterThanOrderBySeqAscIdAsc(int seq);

    List<Wordbook> findBySeqGreaterThanOrderBySeqAsc(int seq);

    List<Wordbook> findTop10ByWordStartsWithOrderByWordAsc(String word);

    List<Wordbook> findTop10ByMeaning1Containing(String key);

    List<Wordbook> findTop10ByMeaning2Containing(String key);

    List<Wordbook> findByMeaning2Containing(String key);

    List<Wordbook> findByMeaning2ContainingOrderByWordAsc(String key);
}