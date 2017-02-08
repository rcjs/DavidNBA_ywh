package android.davidnba.com.davidnba_ywh.ui.interactor;

import android.content.Context;
import android.davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import android.davidnba.com.davidnba_ywh.entity.NavigationEntity;

import java.util.List;

/**
 * Created by 仁昌居士 on 2017/2/6.
 */

public interface HomeInteractor {
    List<BaseLazyFragment> getPagerFragents();
    List<NavigationEntity> getNavigationList(Context context);

}
