package kr.leedox.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private int id;
    private String code;
    private String name;
    private String createDate;
    private String updateDate;
}
