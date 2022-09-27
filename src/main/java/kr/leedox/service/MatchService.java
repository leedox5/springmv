package kr.leedox.service;

import kr.leedox.club.MatchResponse;
import kr.leedox.entity.Game;
import kr.leedox.entity.Member;
import kr.leedox.repository.MatchRepository;
import kr.leedox.entity.Match;
import kr.leedox.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<Match> findByPlayer1(Player p) {
        return matchRepository.findByPlayer1(p);
    }

    private List<Match> findByPlayer2(Player p) {
        return matchRepository.findByPlayer2(p);
    }

    public int getMatchSeq(Player p) {
        int matchSeq = 1;
        matchSeq += getMatchSeq1(p);
        matchSeq += getMatchSeq2(p);
        matchSeq += getMatchSeq3(p);
        matchSeq += getMatchSeq4(p);
        return matchSeq;
    }

    public int getMatchSeq1(Player p) {
        return findByPlayer1(p).size();
    }

    public int getMatchSeq2(Player p) {
        return findByPlayer2(p).size();
    }

    public int getMatchSeq3(Player p) {
        return matchRepository.findByPlayer3(p).size();
    }

    public int getMatchSeq4(Player p) {
        return matchRepository.findByPlayer4(p).size();
    }

    public void deleteByGameId(Integer game_id) {
        matchRepository.deleteByGameId(game_id);
    }

    public String getScore(List<MatchResponse> matches, String username) {

        int cnt1 = 0;
        int cnt2 = 0;
        int cnt3 = 0;
        int cnt4 = 0;
        String sinceStr = "";

        for (int i = 0 ; i < matches.size() ; i++) {
            sinceStr = getSinceStr(matches.get(i).getCreateDate());
            cnt1++;
            matches.get(i).setCreateDate(getDateStr(matches.get(i).getCreateDate()));
            if(matches.get(i).getTeamDesc1().indexOf(username) > -1) {
                if(matches.get(i).getScore1() > matches.get(i).getScore2()) {
                    cnt2++;
                    matches.get(i).setResult("승");
                } else if(matches.get(i).getScore1() < matches.get(i).getScore2()){
                    cnt3++;
                    matches.get(i).setResult("패");
                } else {
                    cnt4++;
                }
            } else {
                if(matches.get(i).getScore1() < matches.get(i).getScore2()) {
                    cnt2++;
                    matches.get(i).setResult("승");
                } else if(matches.get(i).getScore1() > matches.get(i).getScore2()){
                    cnt3++;
                    matches.get(i).setResult("패");
                } else {
                    cnt4++;
                }
            }
        }
        return String.format("%d게임 %d승 %d패(since %s)", cnt1, cnt2, cnt3, sinceStr);
    }

    private String getDateStr(String createDate) {
        /*
        String yy = createDate.substring(2, 4);
        String mm = createDate.substring(4, 6);
        String dd = createDate.substring(6, 8);
        return yy + "." + mm + "." + dd ;
         */
        return createDate.substring(0, 10);
    }

    private String getSinceStr(String createDate) {
        /*
        String yyyy = createDate.substring(0, 4);
        String mm = createDate.substring(4, 6);
        String dd = createDate.substring(6, 8);
        return yyyy + "." + mm + "." + dd ;
         */
        return createDate.substring(0, 10);
    }

    public List<MatchResponse> getScoreMatches(Member member) {
        List<MatchResponse> matchResponses = new ArrayList<>();

        List<Match> matches = matchRepository.findByDescriptionContainsOrderByGameIdDescSeqAsc(member.getUsername());

        for(Match match : matches) {
            if(match.getScore1() != null) {
                if(match.getScore1() > 0 && match.getScore2() > 0) {
                    int pos = match.getDescription().indexOf(":");
                    String desc1 = match.getDescription().substring(0, pos);
                    String desc2 = match.getDescription().substring(pos + 1);

                    MatchResponse matchResponse = new MatchResponse(match.getGame().getCreateDate(), desc1, desc2, match.getScore1(), match.getScore2());
                    matchResponses.add(matchResponse);
                }
            }
        }
        return matchResponses;
    }

    public void save(Match match) {
        matchRepository.save(match);
    }

    public Match getById(Integer match_id) {
        return matchRepository.getById(match_id);
    }
}
