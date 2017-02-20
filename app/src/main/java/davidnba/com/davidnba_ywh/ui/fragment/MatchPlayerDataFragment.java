package davidnba.com.davidnba_ywh.ui.fragment;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.event.RefreshCompleteEvent;
import davidnba.com.davidnba_ywh.event.RefreshEvent;
import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.ui.adapter.MatchPlayerDataAdapter;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.MatchPlayerDataPresenterImpl;
import davidnba.com.davidnba_ywh.ui.view.MatchPlayerDataView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class MatchPlayerDataFragment extends BaseLazyFragment implements MatchPlayerDataView {


    @BindView(R.id.tvPlayerDataLeft)
    TextView tvPlayerDataLeft;
    @BindView(R.id.lvPlayerDataLeft)
    ListView lvPlayerDataLeft;

    @BindView(R.id.tvPlayerDataRight)
    TextView tvPlayerDataRight;
    @BindView(R.id.lvPlayerDataRight)
    ListView lvPlayerDataRight;

    private Presenter presenter;
    private List<MatchStat.MatchStatInfo.StatsBean.PlayerStats> left = new ArrayList<>();
    private List<MatchStat.MatchStatInfo.StatsBean.PlayerStats> right = new ArrayList<>();
    private MatchPlayerDataAdapter leftAdapter;
    private MatchPlayerDataAdapter rightAdapter;

    public static MatchPlayerDataFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchPlayerDataFragment fragment = new MatchPlayerDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_match_player_data);
        ButterKnife.bind(this, getContentView());
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        leftAdapter = new MatchPlayerDataAdapter(left, mActivity, R.layout.item_list_match_player);
        lvPlayerDataLeft.setAdapter(leftAdapter);
        rightAdapter = new MatchPlayerDataAdapter(right, mActivity, R.layout.item_list_match_player);
        lvPlayerDataRight.setAdapter(rightAdapter);
        presenter = new MatchPlayerDataPresenterImpl(mActivity, this, getArguments().getString("mid"));
        presenter.initialized();
    }

    @Override
    public void showPlayerData(List<MatchStat.MatchStatInfo.StatsBean.PlayerStats> playerStatses) {
        boolean isLeft = false;
        boolean isRight = false;
        left.clear();
        right.clear();
        for (MatchStat.MatchStatInfo.StatsBean.PlayerStats item : playerStatses) {
            if (item.subText != null && !isLeft) {
                isLeft = true;
                tvPlayerDataLeft.setText(item.subText);
            } else if (item.subText != null && isLeft) {
                isRight = true;
                tvPlayerDataRight.setText(item.subText);
            } else {
                if (isRight) {
                    right.add(item);
                } else {
                    left.add(item);
                }
            }
        }
        leftAdapter.notifyDataSetChanged();
        rightAdapter.notifyDataSetChanged();
        hideLoading();
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
        hideLoading();
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        EventBus.getDefault().post(new RefreshCompleteEvent());
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
