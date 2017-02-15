package davidnba.com.davidnba_ywh.ui.interactor.impl;


import davidnba.com.davidnba_ywh.app.Constant;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.news.NewsDetail;
import davidnba.com.davidnba_ywh.ui.interactor.NBANewsDetailInteractor;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */
public class NBANewsDetailInteractorImpl implements NBANewsDetailInteractor {
    @Override
    public void getNewsDetail(String arcId, RequestCallback<NewsDetail> callback) {
        TecentService.getNewsDetail(Constant.NewsType.BANNER, arcId, false, callback);
    }
}
