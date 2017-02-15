package davidnba.com.davidnba_ywh.http.bean.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */
public class BaseError implements Serializable {
    @SerializedName("id")
    public int code;
    @SerializedName("text")
    public String msg;
}
