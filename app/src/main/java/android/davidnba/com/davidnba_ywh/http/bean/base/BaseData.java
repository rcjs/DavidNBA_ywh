package android.davidnba.com.davidnba_ywh.http.bean.base;

import java.io.Serializable;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */
public class BaseData implements Serializable {

    /**
     * uid : 4847679
     * status : 200
     * data :
     * msg : 发表成功
     */
    public int uid;
    public int status;
    public String data;
    public String msg;
    public String result;
    public BaseError error;
}
