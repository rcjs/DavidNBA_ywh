package android.davidnba.com.davidnba_ywh.base;

import android.content.DialogInterface;
import android.davidnba.com.davidnba_ywh.widget.LoadingDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* Created by 仁昌居士 on 2017/2/6.
 * 懒加载Fragment</h1> 只有创建并显示的时候才会调用onCreateViewLazy方法
 * 懒加载的原理onCreateView的时候Fragment有可能没有显示出来。
 * 但是调用到setUserVisibleHint(boolean isVisibleToUser),isVisibleToUser =
 * true的时候就说明有显示出来
 * 但是要考虑onCreateView和setUserVisibleHint的先后问题所以才有了下面的代码
 * 注意：
 * 《1》原先的Fragment的回调方法名字后面要加个Lazy，比如Fragment的onCreateView方法， 就写成onCreateViewLazy
 * 《2》使用该LazyFragment会导致多一层布局深度
 */


  /*  fragment一共四种情况：
         可见、初始化；
         可见、未初始化；
         不可见、初始化；
         不可见、未初始化；

         逻辑分析，假设setOffscreenPageLimit（1），共预加载2个fragment：
         刚进来，1是可见，且要初始化的，2是不可见、未初始化，第三个3是不绑定到activity的。
         于是，1走途径1,2走途径2.

         然后，假设滑动了，1滑到2。
           此时，1不可见，且已初始化了，2是可见，将要初始化，3绑定到activity上，为不可见、未初始化。
           于是，1由于预加载的原因，未解除与视图的联系。所以onPause()、onStop()、onDestoryView()未执行，
           并且，由于1不可见，setUserVisibleHint()执行了。所以， isStart设置为false，即不可见;并调用onFragmentStopLazy();
           2可见，同理由于预加载原因，处于可见状态时，不会再调用onCreateView(),而是会调用setUserVisibleHint()，
           所以，isStart设置为true;onFragmentStartLazy();
           3呢，调用onCreateView()，又由于3不可见且未初始化，则不执行。所以，加载的是个FrameLayout
         */
public class BaseLazyFragment extends BaseFragment {
    private boolean isInit = false;//真正要显示的View是否已经被初始化（正常加载）
    private Bundle savedInstanceState;
    public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyload";
    private boolean isLazyLoad = true;
    private FrameLayout layout;
    public LoadingDialog mLoadingDialog;
    private boolean isStart = false;//是否处于可见状态，in the screen

    @Override
    protected void onCreateView(Bundle saveInstanceState) {
        super.onCreateView(saveInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        }
        //判断是否懒加载
        if (isLazyLoad) {
            //一旦isVisibleToUser==true即可对真正需要的显示内容进行加载
            if (getUserVisibleHint() && !isInit) {
                //途径1
                this.savedInstanceState = saveInstanceState;
                onCreateViewLazy(saveInstanceState);
                isInit = true;
            } else {
                //途径2
                //如果不可见或者已经初始化了
                //进行懒加载
                layout = new FrameLayout(getApplicationContext());
                layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                super.setContentView(layout);
            }
        } else {
            //途径3
            //这里是不可见，但已经初始化了
            //不需要懒加载，开门江山，调用onCreateViewLazy正常加载显示内容即可
            onCreateViewLazy(saveInstanceState);
            isInit = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //一旦isVisibleToUser==true即可进行对真正需要的显示内容的加载

        //判断fragment是否可见，是否还未被初始化，该fragment布局是否存在
        //可见，但还没被初始化，且该fragment布局存在
        if (isVisibleToUser && !isInit && getContentView() != null) {
            isInit = true; //允许初始化
            onCreateViewLazy(savedInstanceState);//调用懒加载
            onResumeLazy();
        }
        //对于已经加载过数据的fragment，再次被滑动到也不在进行加载数据，也就是每个fragment仅做一次数据加载工作
        //如果已经初始化了，且该fragment布局存在
        if (isInit && getContentView() != null) {
            if (isVisibleToUser) {
                isStart = true;
                onFragmentStartLazy();
            } else {
                isStart = false;
                onFragmentStopLazy();
            }
        }
    }


    protected void onCreateViewLazy(Bundle savedInstanceState) {
        Log.d("TAG", "onCreateViewLazy() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    //当Fragment被滑到不可见的位置时，调用
    protected void onFragmentStopLazy() {
        Log.d("TAG", "onFragmentStopLazy() called with: " + "");
    }

    //当Fragment被滑到可见的位置时，调用
    protected void onFragmentStartLazy() {
        Log.d("TAG", "onFragmentStartLazy() called with: " + "");

    }

    protected void onResumeLazy() {
        Log.d("TAG", "onResumeLazy() called with: " + "");
    }

    protected void onPauseLazy() {
        Log.d("TAG", "onPauseLazy() called with: " + "");
    }

    protected void onDestroyViewLazy() {

    }


    @Deprecated
    @Override
    public final void onStart() {
        Log.d("TAG", "onStart() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onStart();
        if (isInit && !isStart && getUserVisibleHint()) {
            isStart = true;
            onFragmentStartLazy();
        }
    }

    @Deprecated
    @Override
    public final void onStop() {
        super.onStop();
        Log.d("TAG", "onStop() called: " + "getUserVisibleHint():" + getUserVisibleHint());
        if (isInit && isStart && getUserVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    @Override
    @Deprecated
    public final void onResume() {
        Log.d("TAG", "onResume() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        Log.d("TAG", "onPause() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onPause();
        if (isInit) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public final void onDestroyView() {
        Log.d("TAG", "onDestroyView() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onDestroyView();
        if (isInit) {
            onDestroyViewLazy();
        }
        isInit = false;
    }

    @Override
    public void setContentView(int layoutResID) {
        //判断若isLazyLoad==true,移除所有lazy view，加载真正要显示的view
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            View view = inflater.inflate(layoutResID, layout, false);
            layout.addView(view);
        }
        //否则，开门见山，直接加载真正要显示的view
        else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        //判断若isLazyLoad==true,移除所有lazy view，加载真正要显示的view
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            layout.addView(view);
        }
        //否则，开门见山，直接加载真正要显示的view
        else {
            super.setContentView(view);
        }
    }



      /**
       * 显示刷新Loadding
       */
      public void showLoadingDialog() {
          try {
              mLoadingDialog = LoadingDialog.createDialog(mActivity);
              mLoadingDialog.setTitle(null);
              mLoadingDialog.setCancelable(false);
              mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                  @Override
                  public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                      if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                          hideLoadingDialog();
                      }
                      return true;
                  }
              });
              mLoadingDialog.show();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

      /**
       * 隐藏刷新Loadding
       */
      public void hideLoadingDialog() {
          try {
              if (mLoadingDialog != null) {
                  if (mLoadingDialog.animation != null) {
                      mLoadingDialog.animation.reset();
                  }
                  mLoadingDialog.dismiss();
                  mLoadingDialog = null;
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

}
