package davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;
import davidnba.com.davidnba_ywh.ui.interactor.NBANewsInteractor;
import davidnba.com.davidnba_ywh.ui.interactor.impl.NBANewsInteractorImpl;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.view.NewsView;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public class NBANewsPresenterImpl implements Presenter {
    private Context mContext = null;
    private NewsView mNewsView = null;
    private NBANewsInteractor mNewsInteractor = null;


    public NBANewsPresenterImpl(Context context, NewsView newsView) {
        mContext = context;
        mNewsView = newsView;
        mNewsInteractor = new NBANewsInteractorImpl();
    }

    @Override
    public void initialized() {
        mNewsView.initializeViews(mNewsInteractor.getTabs());
    }
}
