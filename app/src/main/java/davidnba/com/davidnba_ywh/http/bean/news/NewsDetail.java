package davidnba.com.davidnba_ywh.http.bean.news;

import davidnba.com.davidnba_ywh.http.bean.base.Base;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */

public class NewsDetail extends Base {

    public String title;
    @SerializedName("abstract")
    public String abstractX;
    public List<Map<String, String>> content;

    public String url;
    public String imgurl;
    public String imgurl1;
    public String imgurl2;
    public String time;
    public String atype;
    public String commentId;
    public String newsAppId;
}
