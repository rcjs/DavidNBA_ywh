package davidnba.com.davidnba_ywh.support;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.yuyh.library.utils.log.LogUtils;

/**
 * 支持emptyView
 * Created by 仁昌居士 on 2017/2/13.
 */

public class SupportRecyclerView extends RecyclerView {
private View emptyView;

    public SupportRecyclerView(Context context) {
        super(context);
    }

    public SupportRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //adapter数据加载适配
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            LogUtils.i("smy","adapter changed");
            Adapter adapter = getAdapter();
            if(adapter != null && emptyView != null){
                if(adapter.getItemCount() == 0){
                    LogUtils.i("adapter visible");
                    emptyView.setVisibility(View.VISIBLE);
                    SupportRecyclerView.this.setVisibility(View.GONE);
                }else {
                    LogUtils.i("adapter gone");
                    emptyView.setVisibility(View.GONE);
                    SupportRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public void setAdapter(Adapter adapter){
        Adapter oldAdapter = getAdapter();
        if(oldAdapter != null && emptyObserver != null){
            oldAdapter.unregisterAdapterDataObserver(emptyObserver);
        }
        super.setAdapter(adapter);
        if(adapter != null){
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }



    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
