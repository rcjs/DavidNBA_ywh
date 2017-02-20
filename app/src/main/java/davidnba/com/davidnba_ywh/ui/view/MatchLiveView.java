package davidnba.com.davidnba_ywh.ui.view;


import java.util.List;

import davidnba.com.davidnba_ywh.http.bean.match.LiveDetail;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */
public interface MatchLiveView {

    void addList(List<LiveDetail.LiveDetailData.LiveContent> detail, boolean front);

    void showError(String message);
}
