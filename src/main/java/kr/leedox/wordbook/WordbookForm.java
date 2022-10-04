package kr.leedox.wordbook;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class WordbookForm {
    @NotEmpty(message = "단어는 필수항목입니다.")
    @Size(max=50)
    private String word;

    @NotEmpty(message = "설명은 필수항목입니다.")
    private String meaning1;

    private Integer seq;
}
