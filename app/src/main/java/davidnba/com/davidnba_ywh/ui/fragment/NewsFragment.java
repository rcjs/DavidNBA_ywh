package davidnba.com.davidnba_ywh.ui.fragment;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.ui.adapter.VPNewsAdapter;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.NBANewsPresenterImpl;
import davidnba.com.davidnba_ywh.ui.view.NewsView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yuyh.library.utils.DimenUtils;
import com.yuyh.library.view.viewpager.indicator.IndicatorViewPager;
import com.yuyh.library.view.viewpager.indicator.ScrollIndicatorView;
import com.yuyh.library.view.viewpager.indicator.slidebar.DrawableBar;
import com.yuyh.library.view.viewpager.indicator.slidebar.ScrollBar;
import com.yuyh.library.view.viewpager.indicator.transition.OnTransitionTextListener;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by 仁昌居士 on 2017/2/7.
 */

public class NewsFragment extends BaseLazyFragment implements NewsView {
    private IndicatorViewPager indicatorViewPager;
    private ScrollIndicatorView scrollIndicatorView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_nba_news);
        Presenter presenter = new NBANewsPresenterImpl(mActivity, this);
        presenter.initialized();
    }


    @Override
    public void initializeViews(String[] names) {
        scrollIndicatorView = (ScrollIndicatorView) findViewById(R.id.nba_news_indicator);
        scrollIndicatorView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        scrollIndicatorView.setScrollBar(new DrawableBar(mActivity, R.drawable.round_border_white_selector, ScrollBar.Gravity.CENTENT_BACKGROUND) {

            @Override
            public int getHeight(int tabHeight) {
                return tabHeight - DimenUtils.dpToPxInt(12);
            }

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - DimenUtils.dpToPxInt(12);
            }
        });
        //setSplitAuto(boolean splitAuto) 设置是否自动分割
        scrollIndicatorView.setSplitAuto(true);

        // 设置滚动监听
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(getResources().getColor(R.color.colorPrimary), Color.WHITE));


        ViewPager viewPager = (ViewPager) findViewById(R.id.nba_news_viewPager);
        viewPager.setOffscreenPageLimit(names.length);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        indicatorViewPager.setAdapter(new VPNewsAdapter(mActivity, names, getChildFragmentManager()));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            /*3.2运行时改变Menu ItemonCreateOptionsMenu()只有在menu刚被创建时才会执行，因此要想随时动态改变OptionMenu就要实现onPrepareOptionsMenu()方法，该方法会传给你Menu对象，供使用
Android2.3或更低的版本会在每次Menu打开的时候调用一次onPrepareOptionsMenu().
Android3.0及以上版本默认menu是打开的，所以必须调用invalidateOptionsMenu()方法，然后系统将调用onPrepareOptionsMenu()执行update操作。

这样就可以实现我们的自定义菜单，简单方便。*/
            //  mActivity.invalidateOptionsMenu();
        }
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        JCVideoPlayer.releaseAllVideos();
    }
}
