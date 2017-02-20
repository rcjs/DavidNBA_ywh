package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import davidnba.com.davidnba_ywh.event.RefreshCompleteEvent;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.MatchDataView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class MatchDataPresenterImpl implements Presenter {

    private Context context;
    private MatchDataView dataView;

    public MatchDataPresenterImpl(Context context,MatchDataView dataView){
        this.context = context;
        this.dataView = dataView;
    }

    @Override
    public void initialized() {

    }

    public void getMatchStats(String mid,String tabType){
        TecentService.getMatchStat(mid, tabType, new RequestCallback<MatchStat>() {
            @Override
            public void onSuccess(MatchStat matchStat) {
                MatchStat.MatchStatInfo data = matchStat.data;
                if (data != null && data.stats != null) {
                    List<MatchStat.MatchStatInfo.StatsBean> stats = data.stats;
                    for (MatchStat.MatchStatInfo.StatsBean bean : stats) {
                        if (bean.type.equals("12")) {
                            if (bean.goals != null && !bean.goals.isEmpty()) {
                                dataView.showMatchPoint(bean.goals, data.teamInfo);
                            }
                        } else if (bean.type.equals("14")) {
                            if (bean.teamStats != null && !bean.teamStats.isEmpty()) {
                                dataView.showTeamStatistics(bean.teamStats);
                            }
                        }
                    }
                } else {
                    dataView.showError("暂无数据");
                }
                EventBus.getDefault().post(new RefreshCompleteEvent());
            }

            @Override
            public void onFailure(String message) {
                dataView.showError(message);
                EventBus.getDefault().post(new RefreshCompleteEvent());
            }
        });
    }
}
