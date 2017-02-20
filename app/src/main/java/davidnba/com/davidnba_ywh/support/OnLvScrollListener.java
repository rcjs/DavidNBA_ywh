package davidnba.com.davidnba_ywh.support;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */
public class OnLvScrollListener implements OnBottomListener, AbsListView.OnScrollListener {

    private int mListViewHeight = 0;

    public OnLvScrollListener(int mListViewHeight) {
        this.mListViewHeight = mListViewHeight;
    }

    @Override
    public void onBottom() {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(final AbsListView mListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0) {
            View firstVisibleItemView = mListView.getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) { // 顶部
            }
        } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {         // 底部
            View lastVisibleItemView = mListView.getChildAt(mListView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                onBottom();
            }
        }
    }
}