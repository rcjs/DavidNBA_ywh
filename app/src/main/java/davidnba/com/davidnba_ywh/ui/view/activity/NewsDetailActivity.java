package davidnba.com.davidnba_ywh.ui.view.activity;

import android.content.Intent;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseSwipeBackCompatActivity;
import davidnba.com.davidnba_ywh.http.bean.news.NewsDetail;
import davidnba.com.davidnba_ywh.ui.presenter.NewsDetailPresenter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.NewsDetailPresenterImpl;
import davidnba.com.davidnba_ywh.ui.view.NewsDetailView;
import davidnba.com.davidnba_ywh.utils.ImageUtils;
import davidnba.com.davidnba_ywh.widget.PhotoView;

import android.graphics.Bitmap;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.yuyh.library.utils.DimenUtils;
import com.yuyh.library.utils.toast.ToastUtils;
import com.yuyh.library.view.common.Info;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */

public class NewsDetailActivity extends BaseSwipeBackCompatActivity implements NewsDetailView {

    public static final String ARTICLE_ID = "arcId";
    public static final String TITLE = "title";

    @BindView(R.id.llNewsDetail)
    LinearLayout llNewsDetail;
    @BindView(R.id.tvNewsDetailTitle)
    TextView tvNewsDetailTitle;
    @BindView(R.id.tvNewsDetailTime)
    TextView tvNewsDetailTime;

    @BindView(R.id.ivBrowser)
    PhotoView mPhotoView;
    @BindView(R.id.flParent)
    View mParent;
    @BindView(R.id.bg)
    View mBg;

    Info mInfo;
    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    private LayoutInflater inflate;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        showLoadingDialog();
        inflate = LayoutInflater.from(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        } else {
            setTitle("详细新闻");
        }
        String arcId = intent.getStringExtra(ARTICLE_ID);
        initPhotoView();
        NewsDetailPresenter presenter = new NewsDetailPresenterImpl(this, this);
        presenter.initialized(arcId);
    }

    private void initPhotoView() {
        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mPhotoView.setScaleType(ImageView.ScaleType.FIT_START);
        mPhotoView.enable();
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSaveDialog();
                return false;
            }
        });
    }

    @Override
    public void showNewsDetail(NewsDetail newsDetail) {
        tvNewsDetailTime.setText(newsDetail.time);
        tvNewsDetailTitle.setText(newsDetail.title);
        List<Map<String, String>> content = newsDetail.content;
        for (Map<String, String> map : content) {
            Set<String> set = map.keySet();
            if (set.contains("img")) {
                final String url = map.get("img");
                if (!TextUtils.isEmpty(url)) {
                    PhotoView iv = (PhotoView) inflate.inflate(R.layout.imageview_news_detail, null);
                    llNewsDetail.addView(iv);
                    int screenWidth = DimenUtils.getScreenWidth();
                    ViewGroup.LayoutParams lp = iv.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height = screenWidth * 2 / 3;
                    iv.setLayoutParams(lp);
                    iv.setMaxWidth(screenWidth);
                    iv.setMaxHeight(screenWidth);
                    Glide.with(NewsDetailActivity.this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Glide.with(NewsDetailActivity.this).load(url).into(mPhotoView);
                            mInfo = ((PhotoView) v).getInfo();
                            mBg.startAnimation(in);
                            mBg.setVisibility(View.VISIBLE);
                            mParent.setVisibility(View.VISIBLE);
                            mPhotoView.animaFrom(mInfo);
                        }
                    });
                }
            } else {
                if (!TextUtils.isEmpty(map.get("text"))) {
                    TextView tv = (TextView) inflate.inflate(R.layout.textview_news_detail, null);
                    tv.append(map.get("text"));
                    llNewsDetail.addView(tv);
                }
            }

        }
        hideLoadingDialog();
    }

    private void showSaveDialog() {
        final String[] stringItems = {"保存图片"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, mPhotoView);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPhotoView.setDrawingCacheEnabled(true);
                final Bitmap bmp = Bitmap.createBitmap(mPhotoView.getDrawingCache());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ImageUtils.saveImageToGallery(mContext, bmp)) {
                            Looper.prepare();
                            ToastUtils.showToast("保存图片成功");
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            ToastUtils.showToast("保存图片失败");
                            Looper.loop();
                        }
                    }
                }).start();

                mPhotoView.setDrawingCacheEnabled(false);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mParent.getVisibility() == View.VISIBLE) {
            mBg.startAnimation(out);
            mPhotoView.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    mParent.setVisibility(View.GONE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.ivBrowser)
    public void dismissPhotoView() {
        mBg.startAnimation(out);
        mPhotoView.animaTo(mInfo, new Runnable() {
            @Override
            public void run() {
                mParent.setVisibility(View.GONE);
            }
        });
    }

}
