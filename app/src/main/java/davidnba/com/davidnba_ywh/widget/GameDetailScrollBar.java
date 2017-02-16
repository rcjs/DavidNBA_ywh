package davidnba.com.davidnba_ywh.widget;

import android.content.Context;

import com.yuyh.library.view.viewpager.indicator.slidebar.ColorBar;

/**
 * Created by 仁昌居士 on 2017/2/16.
 */

public class GameDetailScrollBar extends ColorBar{
    public GameDetailScrollBar(Context context, int color, int height, Gravity gravity) {
        super(context, color, height, gravity);
    }

    public GameDetailScrollBar(Context context, int color, int height) {
        super(context, color, height);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
}
