package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.MatchCalendar;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.CalendarPageView;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public class CalendarPagePresenter implements Presenter {
    private Context context;
    private CalendarPageView view;

    public CalendarPagePresenter(Context context, CalendarPageView view) {
        this.view = view;
    }

    @Override
    public void initialized() {

    }

    public void getMatchCount(int year, int month) {
        view.showLoadding();
        TecentService.getMatchCalendar(-1, year, month, false, new RequestCallback<MatchCalendar>() {

            @Override
            public void onSuccess(MatchCalendar matchCalendar) {
                if (matchCalendar != null && matchCalendar.data != null) {
                    view.renderMatchCount(matchCalendar.data.matchNum);
                }
                view.hideLoadding();
            }

            @Override
            public void onFailure(String message) {
                view.hideLoadding();
            }
        });
    }
}
