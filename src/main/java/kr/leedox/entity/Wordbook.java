package kr.leedox.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

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

    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "wordbook", cascade = CascadeType.ALL)
    private Collection<WordMeaning> wordMeanings;
}
