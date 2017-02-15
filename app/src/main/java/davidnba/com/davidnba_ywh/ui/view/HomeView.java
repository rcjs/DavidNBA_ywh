package davidnba.com.davidnba_ywh.ui.view;

import davidnba.com.davidnba_ywh.base.BaseLazyFragment;
import davidnba.com.davidnba_ywh.entity.NavigationEntity;

import java.util.List;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public interface HomeView {
    void intializeViews(List<BaseLazyFragment> fragments, List<NavigationEntity> navigationEntityList);
}
