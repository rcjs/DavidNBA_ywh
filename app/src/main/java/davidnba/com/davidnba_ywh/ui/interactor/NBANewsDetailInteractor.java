package davidnba.com.davidnba_ywh.ui.interactor;

import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.bean.news.NewsDetail;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */

public interface NBANewsDetailInteractor {
    void getNewsDetail(String arcId, RequestCallback<NewsDetail> callback);
}
