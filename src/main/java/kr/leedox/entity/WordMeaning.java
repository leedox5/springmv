package kr.leedox.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "word_meaning")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordMeaning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "text")
    private String meaning;
    private String crtDate;
    private String updDate;

    @ManyToOne(targetEntity = Wordbook.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "wordbook_id")
    private Wordbook wordbook;

    @Transient
    private String key;
}
