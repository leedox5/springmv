package kr.leedox.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message="이름은 필수항목입니다.")
    private String name;
    private Integer seq;
    private Integer score01;
    private Integer score11;
    private Integer score02;
    private Integer score12;
    private Integer score03;
    private Integer score13;
    private Integer score04;
    private Integer score14;
    private Integer matchWin;
    private Integer matchLose;
    private Integer gameWin;
    private Integer gameLose;
    private Integer gameSum;
    private Integer matchRank;

    public Player() {
    }

    public Player(Integer id, String name, Integer seq, Integer score01, Integer score11, Integer score02, Integer score12, Integer score03, Integer score13, Integer score04, Integer score14, Integer matchWin, Integer matchLose, Integer gameWin, Integer gameLose, Integer gameSum, Integer matchRank, Game game) {
        this.id = id;
        this.name = name;
        this.seq = seq;
        this.score01 = score01;
        this.score11 = score11;
        this.score02 = score02;
        this.score12 = score12;
        this.score03 = score03;
        this.score13 = score13;
        this.score04 = score04;
        this.score14 = score14;
        this.matchWin = matchWin;
        this.matchLose = matchLose;
        this.gameWin = gameWin;
        this.gameLose = gameLose;
        this.gameSum = gameSum;
        this.matchRank = matchRank;
        this.game = game;
    }

    public Integer getScore01() {
        return score01;
    }

    public void setScore01(Integer score01) {
        this.score01 = score01;
    }

    public Integer getScore11() {
        return score11;
    }

    public void setScore11(Integer score11) {
        this.score11 = score11;
    }

    public Integer getScore02() {
        return score02;
    }

    public void setScore02(Integer score02) {
        this.score02 = score02;
    }

    public Integer getScore12() {
        return score12;
    }

    public void setScore12(Integer score12) {
        this.score12 = score12;
    }

    public Integer getScore03() {
        return score03;
    }

    public void setScore03(Integer score03) {
        this.score03 = score03;
    }

    public Integer getScore13() {
        return score13;
    }

    public void setScore13(Integer score13) {
        this.score13 = score13;
    }

    public Integer getScore04() {
        return score04;
    }

    public void setScore04(Integer score04) {
        this.score04 = score04;
    }

    public Integer getScore14() {
        return score14;
    }

    public void setScore14(Integer score14) {
        this.score14 = score14;
    }

    public Integer getMatchWin() {
        return matchWin;
    }

    public void setMatchWin(Integer matchWin) {
        this.matchWin = matchWin;
    }

    public Integer getMatchLose() {
        return matchLose;
    }

    public void setMatchLose(Integer matchLose) {
        this.matchLose = matchLose;
    }

    public Integer getGameWin() {
        return gameWin;
    }

    public void setGameWin(Integer gameWin) {
        this.gameWin = gameWin;
    }

    public Integer getGameLose() {
        return gameLose;
    }

    public void setGameLose(Integer gameLose) {
        this.gameLose = gameLose;
    }

    public Integer getGameSum() {
        return gameSum;
    }

    public void setGameSum(Integer gameSum) {
        this.gameSum = gameSum;
    }

    public Integer getMatchRank() {
        return matchRank;
    }

    public void setMatchRank(Integer matchRank) {
        this.matchRank = matchRank;
    }

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

}
