package davidnba.com.davidnba_ywh.ui.view;

import java.util.List;

import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.view.base.BaseView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public interface MatchPlayerDataView extends BaseView {
    void showPlayerData(List<MatchStat.MatchStatInfo.StatsBean.PlayerStats> playerStatses);
}
