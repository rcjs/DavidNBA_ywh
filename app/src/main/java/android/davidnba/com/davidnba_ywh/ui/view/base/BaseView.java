package android.davidnba.com.davidnba_ywh.ui.view.base;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public interface BaseView {

    //显示等待对话框时提示信息
    void showLoading(String msg);

    //隐藏等待对话框
    void hideLoading();

    //显示错误信息
    void showError(String msg);
}
