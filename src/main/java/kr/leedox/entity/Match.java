package kr.leedox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "SEQ_MATCH", sequenceName = "SEQ_MATCH", allocationSize = 1)
@Table(name = "GAME_MATCH")
public class Match {
    @Id
    @GeneratedValue(generator = "SEQ_MATCH", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @JsonIgnore
    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @JsonIgnore
    @ManyToOne(targetEntity = Player.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id_1")
    private Player player1;

    @JsonIgnore
    @ManyToOne(targetEntity = Player.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id_2")
    private Player player2;

    @JsonIgnore
    @ManyToOne(targetEntity = Player.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id_3")
    private Player player3;

    @JsonIgnore
    @ManyToOne(targetEntity = Player.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id_4")
    private Player player4;

    private Integer seq;

    @Column( length = 20)
    private String matchDate;

    private String description;
    private Integer score1;
    private Integer score2;

    private Integer matchSeq1;
    private Integer matchSeq2;
    private Integer matchSeq3;
    private Integer matchSeq4;

    @Transient
    private String desc1;

    @Transient
    private String desc2;

    @Transient
    private String desc3;

    @Transient
    private String desc4;
    @Transient
    private String stat;

    public Integer getScore2() {
        return score2;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getScore1() {
        return score1;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer3() {
        return player3;
    }

    public void setPlayer3(Player player3) {
        this.player3 = player3;
    }

    public Player getPlayer4() {
        return player4;
    }

    public void setPlayer4(Player player4) {
        this.player4 = player4;
    }

    public String getDesc1() {
        int pos = this.description.indexOf(":");
        String str = this.description.substring(0, pos);

        return str.split(",")[0];
    }

    public String getDesc2() {
        int pos = this.description.indexOf(":");
        String str = this.description.substring(0, pos);

        return str.split(",")[1];
    }

    public String getDesc3() {
        int pos = this.description.indexOf(":");
        String str = this.description.substring(pos + 1);

        return str.split(",")[0];
    }

    public String getDesc4() {
        int pos = this.description.indexOf(":");
        String str = this.description.substring(pos + 1);

        return str.split(",")[1];
    }

    public String getStat(String username) {

        if(score1 == null) {
            return "X";
        }

        if(getDesc1().contains(username) || getDesc2().contains(username)) {
            if(score1 > score2) {
                return "승";
            } else {
                return "패";
            }
        } else if(getDesc3().contains(username) || getDesc4().contains(username)) {
            if(score1 < score2) {
                return "승";
            } else {
                return "패";
            }
        }
        return "X";
    }
}
