package android.davidnba.com.davidnba_ywh.ui.view.activity;

import android.davidnba.com.davidnba_ywh.base.BaseAppCompatActivity;
import android.davidnba.com.davidnba_ywh.http.bean.match.MatchCalendar;
import android.davidnba.com.davidnba_ywh.ui.view.CalendarPageView;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public class CalendarActivity extends BaseAppCompatActivity implements CalendarPageView {
    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    public void renderMatchCount(MatchCalendar.MatchCalendarBean.MatchNum matchNum) {

    }

    @Override
    public void showLoadding() {

    }

    @Override
    public void hideLoadding() {

    }
}
