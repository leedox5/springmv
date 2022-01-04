package kr.leedox.entity;

import kr.leedox.entity.Game;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int seq;
    private int score01;
    private int score11;
    private int score02;
    private int score12;
    private int score03;
    private int score13;
    private int score04;
    private int score14;
    private int matchWin;
    private int matchLose;
    private int gameWin;
    private int gameLose;
    private int gameSum;
    private int matchRank;

    public Player() {
    }

    public Player(int id, String name, int seq, int score01, int score11, int score02, int score12, int score03, int score13, int score04, int score14, int matchWin, int matchLose, int gameWin, int gameLose, int gameSum, int matchRank, Game game) {
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

    public int getScore01() {
        return score01;
    }

    public void setScore01(int score01) {
        this.score01 = score01;
    }

    public int getScore11() {
        return score11;
    }

    public void setScore11(int score11) {
        this.score11 = score11;
    }

    public int getScore02() {
        return score02;
    }

    public void setScore02(int score02) {
        this.score02 = score02;
    }

    public int getScore12() {
        return score12;
    }

    public void setScore12(int score12) {
        this.score12 = score12;
    }

    public int getScore03() {
        return score03;
    }

    public void setScore03(int score03) {
        this.score03 = score03;
    }

    public int getScore13() {
        return score13;
    }

    public void setScore13(int score13) {
        this.score13 = score13;
    }

    public int getScore04() {
        return score04;
    }

    public void setScore04(int score04) {
        this.score04 = score04;
    }

    public int getScore14() {
        return score14;
    }

    public void setScore14(int score14) {
        this.score14 = score14;
    }

    public int getMatchWin() {
        return matchWin;
    }

    public void setMatchWin(int matchWin) {
        this.matchWin = matchWin;
    }

    public int getMatchLose() {
        return matchLose;
    }

    public void setMatchLose(int matchLose) {
        this.matchLose = matchLose;
    }

    public int getGameWin() {
        return gameWin;
    }

    public void setGameWin(int gameWin) {
        this.gameWin = gameWin;
    }

    public int getGameLose() {
        return gameLose;
    }

    public void setGameLose(int gameLose) {
        this.gameLose = gameLose;
    }

    public int getGameSum() {
        return gameSum;
    }

    public void setGameSum(int gameSum) {
        this.gameSum = gameSum;
    }

    public int getMatchRank() {
        return matchRank;
    }

    public void setMatchRank(int matchRank) {
        this.matchRank = matchRank;
    }

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

}
