package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.bean.news.NewsDetail;
import davidnba.com.davidnba_ywh.ui.interactor.NBANewsDetailInteractor;
import davidnba.com.davidnba_ywh.ui.interactor.impl.NBANewsDetailInteractorImpl;
import davidnba.com.davidnba_ywh.ui.presenter.NewsDetailPresenter;
import davidnba.com.davidnba_ywh.ui.view.NewsDetailView;
import android.support.annotation.NonNull;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */

public class NewsDetailPresenterImpl implements NewsDetailPresenter {

    private Context mContext = null;
    private NewsDetailView mNewsView = null;
    private NBANewsDetailInteractor mNewsDetailInteractor = null;

    public NewsDetailPresenterImpl(Context context, @NonNull NewsDetailView mNewsView) {

        mContext = context;
        this.mNewsView = mNewsView;
        mNewsDetailInteractor = new NBANewsDetailInteractorImpl();
    }

    @Override
    public void initialized(String arcId) {
        mNewsDetailInteractor.getNewsDetail(arcId, new RequestCallback<NewsDetail>() {
            @Override
            public void onSuccess(NewsDetail newsDetail) {
                mNewsView.showNewsDetail(newsDetail);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
