package davidnba.com.davidnba_ywh.ui.fragment;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import android.os.Bundle;

public class BlankFragment extends BaseLazyFragment {
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_blank);
    }
}
