package davidnba.com.davidnba_ywh.ui.adapter;

import android.content.Context;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.api.tencent.TecentService;
import davidnba.com.davidnba_ywh.http.bean.news.NewsItem;
import davidnba.com.davidnba_ywh.http.bean.news.VideoRealUrl;
import davidnba.com.davidnba_ywh.support.NoDoubleClickListener;
import davidnba.com.davidnba_ywh.support.OnListItemClickListener;
import davidnba.com.davidnba_ywh.utils.FrescoUtils;
import davidnba.com.davidnba_ywh.utils.ItemAnimHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuyh.library.utils.DimenUtils;
import com.yuyh.library.utils.log.LogUtils;
import com.zengcanxiang.baseAdapter.recyclerView.HelperAdapter;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public class NewsAdapter extends HelperAdapter<NewsItem.NewsItemBean> {

    private OnListItemClickListener mOnItemClickListener = null;
    private ItemAnimHelper helper = new ItemAnimHelper();

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public NewsAdapter(List<NewsItem.NewsItemBean> data, Context context, int... layoutId) {
        super(data, context, layoutId);
    }

    @Override
    protected void HelperBindData(final com.zengcanxiang.baseAdapter.recyclerView.HelperViewHolder viewHolder, final int position, final NewsItem.NewsItemBean item) {
        if (item.atype.equals("2")) { //视频
            final JCVideoPlayerStandard videoPlayer = viewHolder.getView(R.id.vpVideo);
            if (TextUtils.isEmpty(item.title)) {
                videoPlayer.setUp("", item.realUrl);
                TecentService.getVideoRealUrl(item.vid, new RequestCallback<VideoRealUrl>() {
                    @Override
                    public void onSuccess(VideoRealUrl real) {
                        String vid = TextUtils.isEmpty(real.fn) ? real.vid + ".mp4" : real.fn;
                        String url = real.url + vid + "?vkey=" + real.fvkey;
                        item.realUrl = url;
                        LogUtils.i("title：" + item.title);
                        LogUtils.i("real-url：" + url);
                        videoPlayer.setUp(url, item.title);
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtils.i("real-url：" + message);
                    }
                });
            }else {
                videoPlayer.setUp(item.realUrl,item.title);
            }

            videoPlayer.thumbImageView.setController(FrescoUtils.getController(item.imgurl,videoPlayer.thumbImageView));
            viewHolder.setText(R.id.tvVideoTitle, item.title).setText(R.id.tvVideoTime, item.pub_time);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.height = DimenUtils.getScreenWidth() / 2;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoPlayer.setLayoutParams(params);
        }else{
            SimpleDraweeView iv = viewHolder.getView(R.id.ivBannerImg);
            if (iv != null) { // @bugreport NullPointerException
                iv.setController(FrescoUtils.getController(item.imgurl, iv));
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.height = DimenUtils.getScreenWidth() / 2;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                iv.setLayoutParams(params);
        }
            viewHolder.setText(R.id.tvBannerTitle, item.title)
                    .setText(R.id.tvBannerTime, item.pub_time);

            viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
                }
            });
        }
        helper.showItemAnim(viewHolder.getItemView(), position);
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int checkLayoutIndex(NewsItem.NewsItemBean item, int position) {
        if (item.atype.equals("2"))
            return 1;
        return 0;
    }

}
