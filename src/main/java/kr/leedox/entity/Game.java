package kr.leedox.entity;

import kr.leedox.CalendarUtil;

import javax.persistence.*;
import java.util.Collection;

@Entity
@SequenceGenerator(name = "SEQ_GAME", sequenceName = "SEQ_GAME", allocationSize = 1)
public class Game {
    @Id
    @GeneratedValue(generator = "SEQ_GAME", strategy = GenerationType.SEQUENCE)
    private int id;
    private String subject;
    private String createDate;
    private String creator;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Collection<Player> players;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Collection<Match> matches;

    public Collection<Match> getMatches() {
        return matches;
    }

    public void setMatches(Collection<Match> matches) {
        this.matches = matches;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public Game() {
    }

    public Game(String subject, String creator) {
        this.id = id;
        this.subject = subject;
        this.creator = creator;
        this.createDate = CalendarUtil.formatNow("yyyyMMdd HHmmss");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", createDate='" + createDate + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}
