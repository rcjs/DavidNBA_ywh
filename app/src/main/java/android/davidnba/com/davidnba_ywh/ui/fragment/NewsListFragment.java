package android.davidnba.com.davidnba_ywh.ui.fragment;

import android.content.Intent;
import android.davidnba.com.davidnba_ywh.R;
import android.davidnba.com.davidnba_ywh.app.Constant;
import android.davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import android.davidnba.com.davidnba_ywh.http.bean.news.NewsItem;
import android.davidnba.com.davidnba_ywh.support.OnListItemClickListener;
import android.davidnba.com.davidnba_ywh.support.SupportRecyclerView;
import android.davidnba.com.davidnba_ywh.ui.adapter.NewsAdapter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.yuyh.library.utils.DimenUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public class NewsListFragment extends BaseLazyFragment {
    public static final String INTENT_INT_INDEX = "intent_int_index";
    Constant.NewsType newsType = Constant.NewsType.BANNER;
    private NewsAdapter adapter;

    @InjectView(R.id.refresh)
    MaterialRefreshLayout materialRefreshLayout;
    @InjectView(R.id.recyclerview)
    SupportRecyclerView recyclerView;
    @InjectView(R.id.emptyView)
    View emptyView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_normal_recyclerview);
        ButterKnife.inject(this, getContentView());
        showLoadingDialog();
        newsType = (Constant.NewsType) getArguments().getSerializable(INTENT_INT_INDEX);
        initView();
        requestIndex(false);
    }

    private void requestIndex(boolean b) {

    }

    private void initView() {
        adapter = new NewsAdapter(list, mActivity, R.layout.item_list_news_normal, R.layout.item_list_news_video);
        adapter.setOnItemClickListener(new OnListItemClickListener<NewsItem.NewsItemBean>() {
            @Override
            public void onItemClick(View view, int position, NewsItem.NewsItemBean data) {
                Intent intent;
                switch (newsType) {
                    case VIDEO:
                    case DEPTH:
                    case HIGHLIGHT:
                        intent = new Intent(mActivity, BaseWebActivity.class);
                        intent.putExtra(BaseWebActivity.BUNDLE_KEY_URL, data.url);
                        intent.putExtra(BaseWebActivity.BUNDLE_KEY_TITLE, data.title);
                        startActivity(intent);
                        break;
                    case BANNER:
                    case NEWS:
                    default:
                        intent = new Intent(mActivity, NewsDetailActivity.class);
                        intent.putExtra(NewsDetailActivity.TITLE, data.title);
                        intent.putExtra(NewsDetailActivity.ARTICLE_ID, data.index);
                        startActivity(intent);
                        break;

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(DimenUtils.dpToPxInt(5)));
        materialRefreshLayout.setMaterialRefreshListener(new RefreshListener());

    }
}
