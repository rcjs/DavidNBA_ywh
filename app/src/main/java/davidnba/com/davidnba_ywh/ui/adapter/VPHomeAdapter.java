package davidnba.com.davidnba_ywh.ui.adapter;

import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public class VPHomeAdapter extends FragmentPagerAdapter {
    private List<BaseLazyFragment> mListFragments = null;

    public VPHomeAdapter(FragmentManager fm,List<BaseLazyFragment> fragments) {
        super(fm);
        mListFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
      if(mListFragments != null && position > -1 && position < mListFragments.size()){
          return  mListFragments.get(position);
      }else {
          return null;
      }
    }

    @Override
    public int getCount() {
        return null != mListFragments ? mListFragments.size() : 0;
    }
}
