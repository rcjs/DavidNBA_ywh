package davidnba.com.davidnba_ywh.ui.view;

import java.util.List;

import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.view.base.BaseView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public interface MatchDataView extends BaseView {
    void showMatchPoint(List<MatchStat.MatchStatInfo.StatsBean.Goals> list,MatchStat.MatchStatInfo.MatchTeamInfo teamIOnfo);
    void showTeamStatistics(List<MatchStat.MatchStatInfo.StatsBean.TeamStats> teamStats);
}
