package kr.leedox.club;

import kr.leedox.entity.Match;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResponse {
    private String createDate;
    private String teamDesc1;
    private String teamDesc2;
    private String result;
    private Integer score1;
    private Integer score2;
    public MatchResponse(String p1, String p2, String p3, Integer score1, Integer score2) {
        this.createDate = p1;
        this.teamDesc1 = p2 + " " + score1;
        this.teamDesc2 = p3 + " " + score2;
        this.score1 = score1;
        this.score2 = score2;
    }
}
