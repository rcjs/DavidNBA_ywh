package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;

import java.util.List;

import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.MatchPlayerDataView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class MatchPlayerDataPresenterImpl implements Presenter {

    private Context context;
    private MatchPlayerDataView dataView;
    private String mid;

    public MatchPlayerDataPresenterImpl(Context context, MatchPlayerDataView dataView, String mid) {
        this.context = context;
        this.dataView = dataView;
        this.mid = mid;
    }

    @Override
    public void initialized() {
        dataView.showLoading("");
        TecentService.getMatchStat(mid, "2", new RequestCallback<MatchStat>() {
                    @Override
                    public void onSuccess(MatchStat matchStat) {
                        boolean hasData = false;
                        MatchStat.MatchStatInfo data = matchStat.data;
                        List<MatchStat.MatchStatInfo.StatsBean> stats = data.stats;
                        for (MatchStat.MatchStatInfo.StatsBean bean : stats) {
                            if (bean.type.equals("15")) {
                                if (bean.playerStats != null && !bean.playerStats.isEmpty()) {
                                    dataView.showPlayerData(bean.playerStats);
                                    hasData = true;
                                }
                            }
                        }
                        if (!hasData) {
                            dataView.showError("暂无数据");
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        dataView.hideLoading();
                        dataView.showError(message);
                    }
                }

        );
    }
}