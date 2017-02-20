package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.app.Activity;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.LiveDetail;
import davidnba.com.davidnba_ywh.http.bean.match.LiveIndex;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.MatchLiveView;
import davidnba.com.davidnba_ywh.utils.AlarmTimer;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */
public class MatchLivePresenter implements Presenter {

    private Context context;
    private MatchLiveView liveView;

    private List<String> index = new ArrayList<>();
    private String firstId = "";
    private String lastId = "";

    private String mid;
    private AlarmTimer alarmTimer = null;

    public MatchLivePresenter(Context context, MatchLiveView liveView, String mid) {
        this.context = context;
        this.liveView = liveView;
        this.mid = mid;
    }

    @Override
    public void initialized() {
        alarmTimer = new AlarmTimer() {
            @Override
            public void timeout() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getLiveIndex();
                    }
                });
            }
        };
        alarmTimer.start(6000);
    }

    public void getLiveIndex() {
        TecentService.getMatchLiveIndex(mid, new RequestCallback<LiveIndex>() {
            @Override
            public void onSuccess(LiveIndex liveIndex) {
                if (liveIndex.data != null && liveIndex.data.index != null && !liveIndex.data.index.isEmpty()) {
                    index.clear();
                    index.addAll(liveIndex.data.index);

                    String ids = "";
                    for (int i = 0; i < 20 && i < index.size(); i++) { // 每次最多请求20条
                        if (index.get(i).equals(firstId)) {
                            break;
                        } else {
                            ids += index.get(i) + ",";
                            lastId = index.get(i);
                        }
                    }
                    if (ids.length() > 1) {
                        ids = ids.substring(0, ids.length() - 1);
                        getLiveContent(ids, true);
                    } else {
                        liveView.showError("暂无数据");
                    }
                } else {
                    liveView.showError("暂无数据");
                }
            }

            @Override
            public void onFailure(String message) {
                liveView.showError(message);
            }
        });
    }

    public void getLiveContent(String ids, final boolean front) {
        TecentService.getMatchLiveDetail(mid, ids, new RequestCallback<LiveDetail>() {
            @Override
            public void onSuccess(LiveDetail liveDetail) {
                firstId = index.get(0);
                liveView.addList(liveDetail.data.detail, front);
            }

            @Override
            public void onFailure(String message) {
                liveView.showError(message);
            }
        });
    }

    public void getMoreContent() {
        String ids = "";
        boolean start = false;
        for (int i = 0, sum = 0; sum < 10 && i < index.size(); i++) { // 每次最多请求20条
            if (index.get(i).equals(lastId)) {
                start = true;
            } else {
                if (start) {
                    sum++;
                    ids += index.get(i) + ",";
                    lastId = index.get(i);
                }
            }
        }
        if (ids.length() > 1) {
            ids = ids.substring(0, ids.length() - 1);
            getLiveContent(ids, false);
        }
    }

    public void shutDownTimerTask() {
        if (alarmTimer != null)
            alarmTimer.shutDown();
    }
}
