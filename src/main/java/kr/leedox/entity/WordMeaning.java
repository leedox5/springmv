package kr.leedox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "word_meaning")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordMeaning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private int id;

    @Column(columnDefinition = "text")
    private String meaning;
    private String crtDate;
    private String updDate;

    @JsonIgnore
    @ManyToOne(targetEntity = Wordbook.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "wordbook_id")
    private Wordbook wordbook;

    @JsonIgnore
    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "upper_id")
    private WordMeaning upperMeaning;

    @OneToMany(mappedBy = "upperMeaning")
    private List<WordMeaning> meanings;

    @Transient
    private String key;

    @Transient
    private String header;

    public String getHeader() {
        String[] meanings = meaning.split("\\r?\\n");
        return meanings[0];
    }
}
