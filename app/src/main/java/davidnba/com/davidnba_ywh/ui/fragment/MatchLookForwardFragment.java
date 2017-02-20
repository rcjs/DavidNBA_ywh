package davidnba.com.davidnba_ywh.ui.fragment;

import android.os.Bundle;

import davidnba.com.davidnba_ywh.base.BaseLazyFragment;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class MatchLookForwardFragment extends BaseLazyFragment {

    public static MatchLookForwardFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchLookForwardFragment fragment = new MatchLookForwardFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
