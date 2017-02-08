package android.davidnba.com.davidnba_ywh.ui.presenter.impl;

import android.content.Context;
import android.davidnba.com.davidnba_ywh.ui.interactor.HomeInteractor;
import android.davidnba.com.davidnba_ywh.ui.interactor.impl.HomeInteractorImpl;
import android.davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import android.davidnba.com.davidnba_ywh.ui.view.HomeView;

/**
 * Created by 仁昌居士 on 2017/2/6.
 */

public class HomePresenterImpl implements Presenter {
    private Context mContext = null;
    private HomeView mHomeView = null;
    private HomeInteractor mHomeInteractor = null;

    public HomePresenterImpl(Context context, HomeView homeView) {
        mContext = context;
        mHomeView = homeView;
        mHomeInteractor = new HomeInteractorImpl();
    }

    @Override
    public void initialized() {
        mHomeView.intializeViews(mHomeInteractor.getPagerFragents(), mHomeInteractor.getNavigationList(mContext));
    }
}
