package android.davidnba.com.davidnba_ywh.ui.view;

import android.davidnba.com.davidnba_ywh.http.bean.match.MatchCalendar;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public interface CalendarPageView {
    void renderMatchCount(MatchCalendar.MatchCalendarBean.MatchNum matchNum);

    void showLoadding();

    void hideLoadding();

}
