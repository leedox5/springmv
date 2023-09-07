package kr.leedox.wordbook;

import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WordbookResponse {
    private String username;
    private List<SearchOption> opts;
    private String selOpt;
    private List<Wordbook> words;
    private List<WordMeaning> wordMeanings;
    private Page<Wordbook> paging;
}
