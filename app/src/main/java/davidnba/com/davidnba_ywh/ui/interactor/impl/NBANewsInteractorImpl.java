package davidnba.com.davidnba_ywh.ui.interactor.impl;

import davidnba.com.davidnba_ywh.ui.interactor.NBANewsInteractor;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public class NBANewsInteractorImpl implements NBANewsInteractor {
    @Override
    public String[] getTabs() {
        return new String[]{"今日头条", "新闻资讯", "视频集锦", "最佳进球", "赛场花絮"};
    }
}
