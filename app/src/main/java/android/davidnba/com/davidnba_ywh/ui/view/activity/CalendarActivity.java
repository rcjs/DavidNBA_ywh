package android.davidnba.com.davidnba_ywh.ui.view.activity;

import android.davidnba.com.davidnba_ywh.R;
import android.davidnba.com.davidnba_ywh.base.BaseAppCompatActivity;
import android.davidnba.com.davidnba_ywh.http.bean.match.MatchCalendar;
import android.davidnba.com.davidnba_ywh.ui.presenter.impl.CalendarPagePresenter;
import android.davidnba.com.davidnba_ywh.ui.view.CalendarPageView;
import android.davidnba.com.davidnba_ywh.widget.calendar.CalConstant;
import android.davidnba.com.davidnba_ywh.widget.calendar.CalendarView;
import android.davidnba.com.davidnba_ywh.widget.calendar.ICalendarView;
import android.widget.TextView;

import com.yuyh.library.utils.log.LogUtils;

import java.util.Calendar;

import butterknife.InjectView;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public class CalendarActivity extends BaseAppCompatActivity implements CalendarPageView {

    @InjectView(R.id.calendar)
    CalendarView calendar;
    @InjectView(R.id.tvCalendarDate)
    TextView tvCalendarDate;
    @InjectView(R.id.tvMatchNum)
    TextView tvMatchNum;

    private MatchCalendar.MatchCalendarBean.MatchNum matchNum;
    private CalendarPagePresenter presenter;
    public static final String CALENDAR_DATE = "calendar_data";
    private String date;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initViewsAndEvents() {
        setTitle("日期选择");
        presenter = new CalendarPagePresenter(this, this);
        presenter.getMatchCount(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1);
        calendar.setWeekTextStyle(1);
        tvCalendarDate.setText(getYearMonthText(calendar.getYear(), calendar.getMonth()));
        calendar.setOnRefreshListener(new ICalendarView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvCalendarDate.setText(getYearMonthText(calendar.getYear(), calendar.getMonth()));
            }
        });
        calendar.setOnItemClickListener(new ICalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(int day) {
                int year = calendar.getYear();
                int month = calendar.getMonth();
                String date = year + "-" + month + "-" + day;
                CalendarActivity.this.date = date;
                LogUtils.i("date = " + date);
                showMatchNum(date, day);
            }
        });
    }

    private void showMatchNum(String date, int day) {

    }

    private String getYearMonthText(int year, int month) {
        return new StringBuilder().append(CalConstant.MONTH_NAME[month - 1]).append(", ").append(year).toString();
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
