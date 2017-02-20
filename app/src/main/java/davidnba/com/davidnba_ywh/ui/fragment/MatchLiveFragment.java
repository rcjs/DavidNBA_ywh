package davidnba.com.davidnba_ywh.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ListView;

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
import davidnba.com.davidnba_ywh.http.bean.match.LiveDetail;
import davidnba.com.davidnba_ywh.support.OnLvScrollListener;
import davidnba.com.davidnba_ywh.ui.adapter.MatchLiveAdapter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.MatchLivePresenter;
import davidnba.com.davidnba_ywh.ui.view.MatchLiveView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class MatchLiveFragment extends BaseLazyFragment implements MatchLiveView {

    @BindView(R.id.refresh)
    FrameLayout materialRefreshLayout;
    @BindView(R.id.snlScrollView)
    ListView lvMatchLive;
    @BindView(R.id.emptyView)
    View emptyView;

    private int mListViewHeight = 0;

    private List<LiveDetail.LiveDetailData.LiveContent> list = new ArrayList<>();
    private MatchLiveAdapter adapter;

    private MatchLivePresenter presenter;
    private String mid;

    private boolean isVisibleToUser; // 是否可见。可见才进行刷新

    public static MatchLiveFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchLiveFragment fragment = new MatchLiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_match_live);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, getContentView());
        initData();
    }

    private void initData() {
        showLoadingDialog();
        mid = getArguments().getString("mid");
        adapter = new MatchLiveAdapter(list, mActivity, R.layout.item_list_match_live);
        lvMatchLive.setAdapter(adapter);
        lvMatchLive.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    lvMatchLive.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    lvMatchLive.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mListViewHeight = lvMatchLive.getHeight();
                lvMatchLive.setOnScrollListener(new OnLvScrollListener(mListViewHeight) {
                    @Override
                    public void onBottom() {
                        presenter.getMoreContent();
                    }
                });
            }
        });
        presenter = new MatchLivePresenter(mActivity, this, mid);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            mActivity.invalidateOptionsMenu();
        }
    }

    @Override
    public void addList(List<LiveDetail.LiveDetailData.LiveContent> detail, boolean front) {
        EventBus.getDefault().post(new RefreshCompleteEvent());
        if (front)
            list.addAll(0, detail);
        else
            list.addAll(detail);
        adapter.notifyDataSetChanged();
        hideLoadingDialog();
    }

    @Override
    public void showError(String message) {
        EventBus.getDefault().post(new RefreshCompleteEvent());
        hideLoadingDialog();
        lvMatchLive.setEmptyView(emptyView);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        if (isVisibleToUser) {
            presenter.shutDownTimerTask();
            presenter.initialized();
        }
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        presenter.shutDownTimerTask();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        presenter.shutDownTimerTask();
        presenter.initialized();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
        presenter.shutDownTimerTask();
    }
}
