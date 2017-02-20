package davidnba.com.davidnba_ywh.ui.fragment;

import android.os.Bundle;

import davidnba.com.davidnba_ywh.base.BaseLazyFragment;

/**
 * Created by 仁昌居士 on 2017/2/20.
 *  比赛数据
 */

public class MatchDataFragment extends BaseLazyFragment {

    public static MatchDataFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchDataFragment fragment = new MatchDataFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
