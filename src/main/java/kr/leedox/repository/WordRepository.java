package kr.leedox.repository;

import kr.leedox.entity.Member;
import kr.leedox.entity.Wordbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WordRepository extends JpaRepository<Wordbook, Integer>, JpaSpecificationExecutor<Wordbook> {
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

    List<Wordbook> findByAuthor(Member author);

    List<Wordbook> findByAuthorOrderByUpdDateDesc(Member author);

    Page<Wordbook> findByAuthorOrderByUpdDateDesc(Member member, Pageable pageable);

    int countByMeaning2(String meaning2);
}