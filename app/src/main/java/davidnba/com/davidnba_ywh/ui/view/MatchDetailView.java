package davidnba.com.davidnba_ywh.ui.view;


import davidnba.com.davidnba_ywh.http.bean.match.MatchBaseInfo;
import davidnba.com.davidnba_ywh.ui.view.base.BaseView;

/**
 * Created by 仁昌居士 on 2017/2/16.
 */
public interface MatchDetailView extends BaseView {

    void showTabViewPager(String names[], boolean isStart);

    void showMatchInfo(MatchBaseInfo.BaseInfo matchBaseInfo);
}
