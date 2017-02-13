package android.davidnba.com.davidnba_ywh.support;

import android.view.View;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public interface OnListItemClickListener<T> {
    void onItemClick(View view,int position,T data);
}
