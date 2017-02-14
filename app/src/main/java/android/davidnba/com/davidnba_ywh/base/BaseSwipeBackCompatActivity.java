package android.davidnba.com.davidnba_ywh.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */

public abstract class BaseSwipeBackCompatActivity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //android 自定义ViewSwipeBackHelper,实现左滑结束Activity
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setMoveDown(getResources().getDrawable(com.jude.swipbackhelper.R.drawable.dwPrimary))// 没有办法的办法
                .setSwipeBackEnable(true)//设置是否可滑动
                // .setSwipeEdge(200)//可滑动的范围。px。200表示为左边200px的屏幕
                .setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(1)//sensitiveness of the gesture。0:slow  1:sensitive 对横向滑动手势的敏感程度。0为迟钝 1为敏感
                .setScrimColor(Color.TRANSPARENT)//color of Scrim below the activity 设置用于该掩盖的主要内容,而抽屉打开网眼织物的颜色。底层阴影颜色
                .setClosePercent(0.8f)//close activity when swipe over this 滑动超过 0.8f关闭当前activity  触发关闭Activity百分比
                .setSwipeRelateEnable(true)//if should move together with the following Activity 是否与下一级activity联动。默认是
                .setSwipeRelateOffset(500)//the Offset of following Activity when setSwipeRelateEnable(true) activity联动时的偏移量。默认500px。
                .setDisallowInterceptTouchEvent(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
