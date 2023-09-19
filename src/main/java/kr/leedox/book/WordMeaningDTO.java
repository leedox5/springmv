package kr.leedox.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WordMeaningDTO implements Serializable {
    private String meaning;
    @JsonProperty("wordbook_id")
    private int wordbookId;
}
