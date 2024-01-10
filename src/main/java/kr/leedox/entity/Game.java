package kr.leedox.entity;

import kr.leedox.CalendarUtil;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(length = 20)
    private String gameDate;

    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("seq")
    private Collection<Player> players = new ArrayList<Player>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @OrderBy("seq")
    private Collection<Match> matches;

    @Formula("(select count(*) from player a where a.game_id = id)")
    private int playerCount;

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
        this.subject = subject;
        this.creator = creator;
        this.createDate = CalendarUtil.formatNow("yyyy-MM-dd HH:mm:ss");
    }

    public Game(String subject, Member member) {
        this.subject = subject;
        this.createDate = CalendarUtil.formatNow("yyyy-MM-dd HH:mm:ss");
        this.gameDate = CalendarUtil.formatNow("yyyy-MM-dd HH:mm:ss");
        this.author = member;
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
        return String.format("Game ==> { id=%d, subject='%s', createDate='%s' }", id, subject, createDate);
    }

    public Member getAuthor() {
        return author;
    }

    public String getGameDate() {
        return gameDate;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
