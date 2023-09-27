package kr.leedox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.leedox.book.Book;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "wordbook")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wordbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int seq;
    private String word;
    private String meaning1;
    private String meaning2;
    private String meaning3;
    @Column(columnDefinition = "text")
    private String example1;
    @Column(name = "class")
    private String cate;
    private String crtDate;
    private String updDate;
    private int access;
    private Integer meaningCount;

    @JsonIgnore
    @ManyToOne
    private Member author;

    @JsonIgnore
    @OneToMany(mappedBy = "wordbook", cascade = CascadeType.ALL)
    @OrderBy("crtDate ASC")
    private Collection<WordMeaning> wordMeanings;

    @Formula("(select count(*) from word_meaning a where a.wordbook_id = id)")
    private int memoCount;

    @Formula("(select count(*) from word_meaning a where a.wordbook_id = 51 and instr(substring_index(substring_index(a.meaning,',', -2),',',1),author_id) > 0)")
    private int bookCount;

    @Formula("(select count(*) from wordbook a where a.meaning2 = '3000' and a.word = word)")
    private int basicCount;

    @Transient
    private List<Book> books;
}
