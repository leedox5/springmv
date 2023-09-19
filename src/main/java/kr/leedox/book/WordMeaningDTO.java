package kr.leedox.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class WordMeaningDTO implements Serializable {
    @NotEmpty(message = "내용은 필수 항목 입니다.")
    private String meaning;
    @JsonProperty("wordbook_id")
    private int wordbookId;
}
