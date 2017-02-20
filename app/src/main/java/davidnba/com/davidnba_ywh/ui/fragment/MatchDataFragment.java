package davidnba.com.davidnba_ywh.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuyh.library.utils.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.event.RefreshEvent;
import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.adapter.MatchStatisticsAdapter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.MatchDataPresenterImpl;
import davidnba.com.davidnba_ywh.ui.view.MatchDataView;
import davidnba.com.davidnba_ywh.utils.FrescoUtils;

/**
 * Created by 仁昌居士 on 2017/2/20.
 * 比赛数据
 */

public class MatchDataFragment extends BaseLazyFragment implements MatchDataView {

    @BindView(R.id.snlScrollView)
    ScrollView snlScrollView;

    @BindView(R.id.llMatchPoint)
    LinearLayout llMatchPoint;
    @BindView(R.id.llMatchTeamStatistics)
    LinearLayout llMatchTeamStatistics;

    @BindView(R.id.tvMatchPoint)
    TextView tvMatchPoint;
    @BindView(R.id.tvMatchTeamStatistics)
    TextView tvMatchTeamStatistics;
    @BindView(R.id.lvMatchTeamStatistics)
    ListView lvMatchTeamStatistics;

    @BindView(R.id.llMatchPointHead)
    LinearLayout llMatchPointHead;
    @BindView(R.id.llMatchPointLeft)
    LinearLayout llMatchPointLeft;
    @BindView(R.id.llMatchPointRight)
    LinearLayout llMatchPointRight;

    @BindView(R.id.ivMatchPointLeft)
    SimpleDraweeView ivMatchPointLeft;
    @BindView(R.id.ivMatchPointRight)
    SimpleDraweeView ivMatchPointRight;

    private MatchDataPresenterImpl presenter;
    private MatchStatisticsAdapter adapter;
    private List<MatchStat.MatchStatInfo.StatsBean.TeamStats> teamStats = new ArrayList<>();

    public static MatchDataFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchDataFragment fragment = new MatchDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_match_data);
        ButterKnife.bind(this, getContentView());
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        showLoadingDialog();
        lvMatchTeamStatistics.setFocusable(false);
        presenter = new MatchDataPresenterImpl(mActivity, this);
        presenter.initialized();
        presenter.getMatchStats(getArguments().getString("mid"), "1");
    }

    @Override
    public void showMatchPoint(List<MatchStat.MatchStatInfo.StatsBean.Goals> list, MatchStat.MatchStatInfo.MatchTeamInfo teamIOnfo) {
        ivMatchPointLeft.setController(FrescoUtils.getController(teamIOnfo.leftBadge, ivMatchPointLeft));
        ivMatchPointRight.setController(FrescoUtils.getController(teamIOnfo.rightBadge, ivMatchPointRight));
        MatchStat.MatchStatInfo.StatsBean.Goals goals = list.get(0);
        List<String> head = goals.head;
        List<String> left = goals.rows.get(0);
        List<String> right = goals.rows.get(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        for (int i = 0; i < head.size() && i < left.size() && i < right.size(); i++) {
            if (llMatchPointRight.getChildAt(i + 1) != null) {
                TextView tv = (TextView) llMatchPointHead.getChildAt(i + 1);
                tv.setText(head.get(i));
            } else {
                TextView tv = (TextView) inflater.inflate(R.layout.tab_match_point, null);
                tv.setText(head.get(i));
                tv.setLayoutParams(params);
                llMatchPointHead.addView(tv, i + 1);
            }
            if (left != null) {
                if (llMatchPointLeft.getChildAt(i + 1) != null) {
                    TextView tv1 = (TextView) llMatchPointLeft.getChildAt(i + 1);
                    tv1.setText(left.get(i));
                } else {
                    TextView tv1 = (TextView) inflater.inflate(R.layout.tab_match_point, null);
                    tv1.setText(left.get(i));
                    tv1.setLayoutParams(params);
                    llMatchPointLeft.addView(tv1, i + 1);
                }
            }
            if (right != null) {
                if (llMatchPointRight.getChildAt(i + 1) != null) {
                    TextView tv2 = (TextView) llMatchPointRight.getChildAt(i + 1);
                    tv2.setText(right.get(i));
                } else {
                    TextView tv2 = (TextView) inflater.inflate(R.layout.tab_match_point, null);
                    tv2.setText(right.get(i));
                    tv2.setLayoutParams(params);
                    llMatchPointRight.addView(tv2, i + 1);
                }
            }
        }
        llMatchPoint.setVisibility(View.VISIBLE);
        complete();
    }

    @Override
    public void showTeamStatistics(List<MatchStat.MatchStatInfo.StatsBean.TeamStats> teamStats) {
        this.teamStats.clear();
        this.teamStats.addAll(teamStats);
        if (adapter == null) {
            adapter = new MatchStatisticsAdapter(this.teamStats, mActivity, R.layout.item_list_match_team_statistics);
            lvMatchTeamStatistics.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        llMatchTeamStatistics.setVisibility(View.VISIBLE);
        complete();
        snlScrollView.scrollTo(0,0);
    }
    public void complete() {
        snlScrollView.smoothScrollTo(0, 20);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingDialog();
            }
        }, 1000);
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
        ToastUtils.showSingleToast(msg);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        presenter.getMatchStats(getArguments().getString("mid"), "1");
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
