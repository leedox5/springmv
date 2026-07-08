package kr.leedox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        name = "site_message",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_SITE_MESSAGE_01", columnNames = {"message_code", "locale"})
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "message_code", nullable = false)
    private String code;

    @Column(nullable = false)
    private String locale;

    @Column(columnDefinition = "text", nullable = false)
    private String content;
}
