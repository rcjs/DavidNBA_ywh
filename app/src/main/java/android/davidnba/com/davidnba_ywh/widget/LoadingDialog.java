package android.davidnba.com.davidnba_ywh.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.davidnba.com.davidnba_ywh.R;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by 仁昌居士 on 2017/2/6.
 */

public class LoadingDialog extends Dialog {
    private static LoadingDialog loadingDialog = null;
    public Animation animation;


    public LoadingDialog(Context context) {
        super(context);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static LoadingDialog createDialog(Context context) {
        loadingDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return loadingDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (loadingDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) loadingDialog.findViewById(R.id.ivLoading);
        float curTranslationY = imageView.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", curTranslationY,
                -imageView.getHeight() / 3, curTranslationY, imageView.getHeight() / 3, curTranslationY);
        animator.setDuration(1500);
        animator.setRepeatMode(Animation.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }
}
