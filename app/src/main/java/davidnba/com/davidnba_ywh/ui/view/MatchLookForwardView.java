package davidnba.com.davidnba_ywh.ui.view;


import java.util.List;

import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */
public interface MatchLookForwardView {

    void showTeamInfo(MatchStat.MatchStatInfo.MatchTeamInfo info);

    void showMaxPlayer(List<MatchStat.MatchStatInfo.StatsBean.MaxPlayers> maxPlayers);

    void showHistoryMatchs(List<MatchStat.MatchStatInfo.StatsBean.VS> vs);

    void showRecentMatchs(MatchStat.MatchStatInfo.StatsBean.TeamMatchs teamMatches);

    void showFutureMatchs(MatchStat.MatchStatInfo.StatsBean.TeamMatchs teamMatches);

    void showError(String message);

}
