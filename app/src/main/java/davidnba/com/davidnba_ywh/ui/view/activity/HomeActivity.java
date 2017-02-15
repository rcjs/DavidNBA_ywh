package davidnba.com.davidnba_ywh.ui.view.activity;

import android.content.Intent;
import android.content.res.Configuration;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.app.Constant;
import davidnba.com.davidnba_ywh.base.BaseAppCompatActivity;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.entity.NavigationEntity;
import davidnba.com.davidnba_ywh.event.CalendarEvent;
import davidnba.com.davidnba_ywh.ui.adapter.VPHomeAdapter;
import davidnba.com.davidnba_ywh.ui.presenter.Presenter;
import davidnba.com.davidnba_ywh.ui.presenter.impl.HomePresenterImpl;
import davidnba.com.davidnba_ywh.ui.view.HomeView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yuyh.library.permission.Acp;
import com.yuyh.library.permission.AcpListener;
import com.yuyh.library.permission.AcpOptions;
import com.yuyh.library.utils.DeviceUtils;
import com.yuyh.library.utils.toast.ToastUtils;
import com.zengcanxiang.baseAdapter.absListView.HelperAdapter;
import com.zengcanxiang.baseAdapter.absListView.HelperViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by 仁昌居士 on 2017/2/6.
 */

public class HomeActivity extends BaseAppCompatActivity implements HomeView {

    /*@InjectView(R.id.home_container)
    XViewPager mViewPager;*/
    @BindView(R.id.home_container)
    ViewPager mViewPager;
    @BindView(R.id.home_navigation_list)
    ListView mNavListView;
    @BindView(R.id.home_drawer)
    DrawerLayout mDrawerLayout;


    private ActionBarDrawerToggle mActionBarDrawerToggle = null;
    private HelperAdapter<NavigationEntity> mNavListAdapter = null;

    private static long DOUBLE_CLICK_TIME = 0L;
    private static int REQUEST_DATE_CODE = 1;
    private int mCurrentMenuCheckedPos = 0;
    private Presenter presenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        presenter = new HomePresenterImpl(this, this);
        presenter.initialized();
        BmobUpdateAgent.setUpdateOnlyWifi(true); // wifi下面才提示APP更新
        BmobUpdateAgent.update(this);

        //Androird6.0 权限申请
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE)
                        .setRationalMessage("以下权限需要您授权，否则将不能正常使用APP。\n" +
                                "1、读取SD卡权限\n" +
                                "2、读取手机IMEI").build(),
                new AcpListener() {
                    //权限通过
                    @Override
                    public void onGranted() {
                        Constant.deviceId = DeviceUtils.getIMEI(HomeActivity.this);
                    }

                    //权限被拒绝
                    @Override
                    public void onDenied(List<String> permissions) {
                        String str = "";
                        for (String permission : permissions) {
                            str += permission + "\n";
                        }
                        ToastUtils.showSingleLongToast(str + "权限拒绝，可能会引起APP异常退出");
                    }
                });
    }


    @Override
    public void intializeViews(List<BaseLazyFragment> fragments, List<NavigationEntity> navigationEntityList) {
        if (null != fragments && !fragments.isEmpty()) {
          //  mViewPager.setEnableScroll(false);
            Log.d("rcjs","rcjs"+fragments.size());

          /*  mViewPager = (ViewPager) findViewById(R.id.home_container) ;
           mNavListView = (ListView) findViewById(R.id.home_navigation_list) ;
           mDrawerLayout= (DrawerLayout) findViewById(R.id.home_drawer) ;*/

            mViewPager.setOffscreenPageLimit(fragments.size());
            mViewPager.setAdapter(new VPHomeAdapter(getSupportFragmentManager(), fragments));
        }

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (null != mNavListAdapter) {
                    //设置Activity的name显示为左侧菜单栏的选中的item的名字，即toolbar上的
                    setTitle(((NavigationEntity) mNavListAdapter.getItem(mCurrentMenuCheckedPos)).getName());
                }
            }
        };
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mNavListAdapter = new HelperAdapter<NavigationEntity>(navigationEntityList, HomeActivity.this, R.layout.item_list_navigation) {
            @Override
            public void HelpConvert(HelperViewHolder viewHolder, int position, NavigationEntity item) {
                viewHolder.setImageResource(R.id.list_item_navigation_icon, item.getIconResId())
                        .setText(R.id.list_item_navigation_name, item.getName());
            }
        };

        mNavListView.setAdapter(mNavListAdapter);
        mNavListAdapter.notifyDataSetChanged();

        mNavListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentMenuCheckedPos = position;
                mNavListAdapter.notifyDataSetChanged();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mViewPager.setCurrentItem(mCurrentMenuCheckedPos, false);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    ToastUtils.showLongToast("再按一次退出");
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*
    参考网址
    http://stormzhang.com/android/2014/09/14/activity-lifecycle1/
    onPostCreate方法是指onCreate方法彻底执行完毕的回调，onPostResume类似，
      这两个方法官方说法是一般不会重写，现在知道的做法也就只有在使用ActionBarDrawerToggle的使用在onPostCreate需要在屏幕旋转时候等同步下状态*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.syncState();
        }
    }

    /*在一些特殊的情况中，你可能希望当一种或者多种配置改变时避免重新启动你的activity。你可以通过在manifest中设置android:configChanges属性来实现这点。
    你可以在这里声明activity可以处理的任何配置改变，当这些配置改变时不会重新启动activity，而会调用activity的
    onConfigurationChanged(Resources.Configuration)方法。如果改变的配置中包含了你所无法处理的配置（在android:configChanges并未声明），
    你的activity仍然要被重新启动，而onConfigurationChanged(Resources.Configuration)将不会被调用。*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    /*onPrepareOptionsMenu：
       onPrepareOptionsMenu是每次在display Menu之前，都会去调用，只要按一次Menu按鍵，就会调用一次。所以可以在这里动态的改变menu。*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = this.getMenuInflater();
        switch (mCurrentMenuCheckedPos) {
            case 1:
                inflater.inflate(R.menu.menu_schedule, menu);
                break;
            default:
                //   inflater.inflate(R.menu.menu_home, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /*  onCreateOptionsMenu：
        只会调用一次，他只会在Menu显示之前去调用一次，之后就不会在去调用。
        返回true则显示该menu,false 则不显示;
        */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * 菜单项被点击时调用，也就是菜单项的监听方法。
         * 通过这几个方法，可以得知，对于Activity，同一时间只能显示和监听一个Menu 对象。 TODO Auto-generated
         */

        if (mActionBarDrawerToggle != null && mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_calendar:
                startActivityForResult(new Intent(this, CalendarActivity.class), REQUEST_DATE_CODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示overflower菜单图标
     * 利用反射让隐藏在Overflow中的MenuItem显示Icon图标,就是那个日历图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DATE_CODE && resultCode == RESULT_OK) {
            String date = data.getStringExtra(CalendarActivity.CALENDAR_DATE);
            if (!TextUtils.isEmpty(date))
                EventBus.getDefault().post(new CalendarEvent(date));
        }
    }
}

