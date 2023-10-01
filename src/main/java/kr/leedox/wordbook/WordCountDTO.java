package kr.leedox.wordbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WordCountDTO {
    private String updDate;
    private Long count;
}
