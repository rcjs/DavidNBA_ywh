package android.davidnba.com.davidnba_ywh.entity;

import java.io.Serializable;

/**
 * Created by 仁昌居士 on 2017/2/7.
 * Serializable要改成更好的Parcelable
 */

public class NavigationEntity implements Serializable {

    private int iconResId;
    private String name;

    public NavigationEntity(int iconResId, String name) {
        this.iconResId = iconResId;
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
