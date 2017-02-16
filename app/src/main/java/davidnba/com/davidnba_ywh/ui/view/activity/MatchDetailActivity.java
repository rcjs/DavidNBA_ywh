package davidnba.com.davidnba_ywh.ui.view.activity;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuyh.library.utils.DimenUtils;
import com.yuyh.library.view.viewpager.indicator.IndicatorViewPager;
import com.yuyh.library.view.viewpager.indicator.ScrollIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseSwipeBackCompatActivity;
import davidnba.com.davidnba_ywh.event.RefreshEvent;
import davidnba.com.davidnba_ywh.http.bean.match.MatchBaseInfo;
import davidnba.com.davidnba_ywh.ui.adapter.VPGameDetailAdapter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.MatchDetailPresenter;
import davidnba.com.davidnba_ywh.ui.view.MatchDetailView;
import davidnba.com.davidnba_ywh.utils.FrescoUtils;
import davidnba.com.davidnba_ywh.widget.GameDetailScrollBar;
import davidnba.com.davidnba_ywh.widget.StickyNavLayout;

/**
 * Created by 仁昌居士 on 2017/2/16.
 */

public class MatchDetailActivity extends BaseSwipeBackCompatActivity implements MatchDetailView, StickyNavLayout.OnStickStateChangeListener {
    public static final String INTENT_MID = "mid";
    private String mid;

    @BindView(R.id.snlViewPager)
    ViewPager viewPager;
    @BindView(R.id.snlIindicator)
    ScrollIndicatorView indicator;
    @BindView(R.id.stickyNavLayout)
    StickyNavLayout stickyNavLayout;

    @BindView(R.id.rlMatchToolbar)
    RelativeLayout rlMatchToolbar;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvMatchTitle)
    TextView tvMatchTitle;
    @BindView(R.id.tvLeftRate)
    TextView tvLeftRate;
    @BindView(R.id.tvMatchState)
    TextView tvMatchState;
    @BindView(R.id.tvRightRate)
    TextView tvRightRate;
    @BindView(R.id.tvMatchLeftScore)
    TextView tvMatchLeftScore;
    @BindView(R.id.tvMatchType)
    TextView tvMatchType;
    @BindView(R.id.tvMatchRightScore)
    TextView tvMatchRightScore;
    @BindView(R.id.tvMatchStartTime)
    TextView tvMatchStartTime;

    @BindView(R.id.ivMatchLeftTeam)
    SimpleDraweeView ivMatchLeftTeam;
    @BindView(R.id.ivMatchRightTeam)
    SimpleDraweeView ivMatchRightTeam;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private IndicatorViewPager indicatorViewPager;
    private VPGameDetailAdapter adapter;
    private MatchDetailPresenter presenter;
    private boolean isNeedUpdateTab = true;
    private boolean lastIsTopHidden;//记录上次是否悬浮

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_game_detail;
    }


    @Override
    protected void initViewsAndEvents() {
        EventBus.getDefault().register(this);
        mid = getIntent().getStringExtra(INTENT_MID);
        rlMatchToolbar.getBackground().setAlpha(0);
        indicator.setScrollBar(new GameDetailScrollBar(getApplicationContext(), getResources().getColor(R.color.colorPrimary), DimenUtils.dpToPxInt(3)));
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        stickyNavLayout.setOnStickStateChangeListener(this);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        presenter = new MatchDetailPresenter(this, this);
        presenter.getMatchBaseInfo(mid);
    }

    @Override
    public void isStick(boolean isStick) {
        if (lastIsTopHidden != isStick) {
            lastIsTopHidden = isStick;
        }
    }

    @Override
    public void scrollPercent(float percent) {
        rlMatchToolbar.getBackground().setAlpha((int) ((float) 255 * percent));
        if (percent == 0) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        } else {
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setOnRefreshListener(null);
        }
    }

    @Override
    public void showTabViewPager(String[] names, boolean isStart) {
        isNeedUpdateTab = false;
        hideLoadingDialog();
        adapter = new VPGameDetailAdapter(this, names, getSupportFragmentManager(), mid, isStart);
        indicatorViewPager.setAdapter(adapter);
    }

    @Override
    public void showMatchInfo(MatchBaseInfo.BaseInfo info) {
        tvMatchTitle.setText(info.leftName + "vs" + info.rightName);
        if (!TextUtils.isEmpty(info.leftWins) && !TextUtils.isEmpty(info.leftLosses))
            tvLeftRate.setText(info.leftWins + "胜" + info.leftLosses + "负");
        if (!TextUtils.isEmpty(info.rightWins) && !TextUtils.isEmpty(info.rightLosses))
            tvRightRate.setText(info.rightWins + "胜" + info.rightLosses + "负");
        String startTime = info.startDate + info.startHour;
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH:mm");
        String state = "未开始";
        try {
            Date date = format.parse(startTime);
            String todayStr = format.format(new Date());
            Date today = format.parse(todayStr);
            if (date.getTime() > today.getTime()) { // 未开始
                if (isNeedUpdateTab)
                    presenter.getTab(false);
            } else {
                state = info.quarterDesc;
                if (((state.contains("第4节") || state.contains("加时")) && !info.leftGoal.equals(info.rightGoal))
                        && state.contains("00:00")) {
                    state = "已结束";
                }
                if (isNeedUpdateTab)
                    presenter.getTab(true);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvMatchState.setText(state);
        tvMatchType.setText(info.desc);
        tvMatchStartTime.setText(info.startDate + "   " + info.startHour + "   " + info.venue);
        tvMatchLeftScore.setText(info.leftGoal);
        tvMatchRightScore.setText(info.rightGoal);
        ivMatchLeftTeam.setController(FrescoUtils.getController(info.leftBadge, ivMatchLeftTeam));
        ivMatchRightTeam.setController(FrescoUtils.getController(info.rightBadge, ivMatchRightTeam));
    }

    @Override
    public void showLoading(String msg) {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.getMatchBaseInfo(mid);
            EventBus.getDefault().post(new RefreshEvent());
        }
    };
}
