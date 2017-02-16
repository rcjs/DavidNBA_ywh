package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;

import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.MatchBaseInfo;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.MatchDetailView;


/**
 * Created by 仁昌居士 on 2017/2/16.
 */
public class MatchDetailPresenter implements Presenter {

    private Context context;
    private MatchDetailView detailView;

    public MatchDetailPresenter(Context context, MatchDetailView detailView) {
        this.context = context;
        this.detailView = detailView;
    }

    @Override
    public void initialized() {
    }

    public void getTab(boolean isStart) {
        String names[];
        if (isStart) {
            names = new String[]{"比赛数据", "技术统计", "图文直播"};
        } else {
            names = new String[]{"比赛前瞻", "图文直播"};
        }
        detailView.showTabViewPager(names, isStart);
    }

    public void getMatchBaseInfo(String mid) {
        detailView.showLoading("");
        TecentService.getMatchBaseInfo(mid, new RequestCallback<MatchBaseInfo.BaseInfo>() {
            @Override
            public void onSuccess(MatchBaseInfo.BaseInfo matchBaseInfo) {
                detailView.showMatchInfo(matchBaseInfo);
                detailView.hideLoading();
            }

            @Override
            public void onFailure(String message) {
                detailView.hideLoading();
            }
        });
    }
}
