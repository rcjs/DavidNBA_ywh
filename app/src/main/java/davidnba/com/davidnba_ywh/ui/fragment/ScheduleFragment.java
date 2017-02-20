package davidnba.com.davidnba_ywh.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.yuyh.library.utils.DateUtils;
import com.yuyh.library.utils.DimenUtils;
import com.yuyh.library.utils.log.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.event.CalendarEvent;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.match.Matchs;
import davidnba.com.davidnba_ywh.http.bean.player.StatsRank;
import davidnba.com.davidnba_ywh.support.OnListItemClickListener;
import davidnba.com.davidnba_ywh.support.SpaceItemDecoration;
import davidnba.com.davidnba_ywh.support.SupportRecyclerView;
import davidnba.com.davidnba_ywh.ui.adapter.ScheduleAdapter;
import davidnba.com.davidnba_ywh.ui.view.activity.MatchDetailActivity;

import static android.R.attr.data;
import static rx.Completable.complete;

/**
 * Created by 仁昌居士 on 2017/2/7.
 */

public class ScheduleFragment extends BaseLazyFragment {
    private String date = "";
    @BindView(R.id.refresh)
    MaterialRefreshLayout materialRefreshLayout;
    @BindView(R.id.recyclerview)
    SupportRecyclerView recyclerView;
    @BindView(R.id.emptyView)
    View emptyView;
    private ScheduleAdapter adapter;
    private List<Matchs.MatchsDataBean.MatchesBean> list = new ArrayList<>();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_normal_recyclerview);
        ButterKnife.bind(this, getContentView());
        date = DateUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");
        LogUtils.i(date);
        EventBus.getDefault().register(this);//注入eventbus

        mActivity.invalidateOptionsMenu();

        initView();
        requestMatchs(date, true);
    }

    private void requestMatchs(String date, boolean isRefresh) {
        showLoadingDialog();
        TecentService.getMatchsByDate(date, isRefresh, new RequestCallback<Matchs>() {
            @Override
            public void onSuccess(Matchs matchs) {
                list.clear();
                List<Matchs.MatchsDataBean.MatchesBean> mList = matchs.getData().matches;
                if (!mList.isEmpty()) {
                    for (Matchs.MatchsDataBean.MatchesBean bean : mList) {
                        list.add(bean);
                    }
                }
                complete();
            }

            @Override
            public void onFailure(String message) {
                complete();
            }
        });
    }

    private void initView() {
        adapter = new ScheduleAdapter(list, mActivity, R.layout.item_list_match);
        adapter.setOnItemClickListener(new OnListItemClickListener<Matchs.MatchsDataBean.MatchesBean>() {
            @Override
            public void onItemClick(View view, int position, Matchs.MatchsDataBean.MatchesBean data) {
                Intent intent = new Intent(mActivity, MatchDetailActivity.class);
                intent.putExtra(MatchDetailActivity.INTENT_MID, data.matchInfo.mid);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(DimenUtils.dpToPxInt(3)));
        materialRefreshLayout.setMaterialRefreshListener(new RefreshListener());
        materialRefreshLayout.setLoadMore(false);
    }


    private class RefreshListener extends MaterialRefreshListener {
        @Override
        public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
            requestMatchs(date, true);
        }
    }

    private void complete() {
        recyclerView.setEmptyView(emptyView);
        adapter.notifyDataSetChanged();
        materialRefreshLayout.finishRefresh();
        materialRefreshLayout.finishRefreshLoadMore();
        hideLoadingDialog();
    }


    @Subscribe
    public void onEventMainThread(CalendarEvent msg) {
        date = msg.getDate();
        LogUtils.i(msg.getDate());
        requestMatchs(date, true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            mActivity.invalidateOptionsMenu();
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);//注销eventbus
    }
}
