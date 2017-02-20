package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;



import org.greenrobot.eventbus.EventBus;

import java.util.List;

import davidnba.com.davidnba_ywh.event.RefreshCompleteEvent;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.MatchLookForwardView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class MatchLookForwardPresenter implements Presenter {

    private Context context;
    private MatchLookForwardView forwardView;

    public MatchLookForwardPresenter(Context context, MatchLookForwardView forwardView) {
        this.context = context;
        this.forwardView = forwardView;
    }

    @Override
    public void initialized() {
    }

    public void getMatchStat(String mid, String tabType) {
        TecentService.getMatchStat(mid, tabType, new RequestCallback<MatchStat>() {
            @Override
            public void onSuccess(MatchStat matchStat) {
                MatchStat.MatchStatInfo data = matchStat.data;
                forwardView.showTeamInfo(data.teamInfo);
                List<MatchStat.MatchStatInfo.StatsBean> stats = data.stats;
                for (MatchStat.MatchStatInfo.StatsBean bean : stats) {
                    if (bean.type.equals("1")) {
                        if (bean.vs != null && !bean.vs.isEmpty()) {
                            forwardView.showHistoryMatchs(bean.vs);
                        }
                    } else if (bean.type.equals("2")) {
                        if (bean.teamMatches != null && bean.teamMatches.left != null && bean.teamMatches.right != null) {
                            forwardView.showRecentMatchs(bean.teamMatches);
                        }
                    } else if (bean.type.equals("3")) {
                        if (bean.teamMatches != null && bean.teamMatches.left != null && bean.teamMatches.right != null) {
                            forwardView.showFutureMatchs(bean.teamMatches);
                        }
                    } else if (bean.type.equals("13")) {
                        if (bean.maxPlayers != null && !bean.maxPlayers.isEmpty()) {
                            forwardView.showMaxPlayer(bean.maxPlayers);
                        }
                    }
                }
                EventBus.getDefault().post(new RefreshCompleteEvent());
            }

            @Override
            public void onFailure(String message) {
                forwardView.showError(message);
                EventBus.getDefault().post(new RefreshCompleteEvent());
            }
        });
    }
}
