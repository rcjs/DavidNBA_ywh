package davidnba.com.davidnba_ywh.ui.interactor.impl;

import android.content.Context;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.entity.NavigationEntity;
import davidnba.com.davidnba_ywh.ui.fragment.NewsFragment;
import davidnba.com.davidnba_ywh.ui.interactor.HomeInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 仁昌居士 on 2017/2/6.
 */

public class HomeInteractorImpl implements HomeInteractor{
    /*@Override
    public List<BaseLazyFragment> getPagerFragents() {
        List<BaseLazyFragment> fragments = new ArrayList<BaseLazyFragment>() {{
            add(new NewsFragment());
            add(new ScheduleFragment());
            add(new TeamSortFragment());
            add(new StatsRankFragment());
            add(new ForumListFragment());
            add(new OtherFragment());
        }};
        return fragments;
    }*/


    @Override
    public List<BaseLazyFragment> getPagerFragents() {
        List<BaseLazyFragment> fragments = new ArrayList<BaseLazyFragment>() {{
            add(new NewsFragment());
            add(new NewsFragment());
            add(new NewsFragment());
            add(new NewsFragment());
            add(new NewsFragment());
            add(new NewsFragment());
        }};
        return fragments;
    }



    @Override
    public List<NavigationEntity> getNavigationList(Context context) {
        List<NavigationEntity> navigationEntities = new ArrayList<NavigationEntity>() {{
            add(new NavigationEntity(R.drawable.ic_news, "NBA头条"));
            add(new NavigationEntity(R.drawable.ic_video, "赛事直播"));
            add(new NavigationEntity(R.drawable.ic_format, "球队战绩"));
            add(new NavigationEntity(R.drawable.ic_format, "数据排行"));
            add(new NavigationEntity(R.drawable.ic_favorite, "虎扑专区"));
            add(new NavigationEntity(R.drawable.ic_other, "其他"));
        }};
        return navigationEntities;
    }
}
