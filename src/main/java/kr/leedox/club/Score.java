package kr.leedox.club;

import kr.leedox.entity.Match;
import kr.leedox.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Score {
    private Member member;
    private String score;
    private List<MatchResponse> matches;
    public Score(Member member) {
        this.member = member;
    }
}
