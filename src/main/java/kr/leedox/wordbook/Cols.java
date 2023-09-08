package kr.leedox.wordbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Cols {
    private String id;
    private String header;
    private boolean visible;
    private String align;
}
