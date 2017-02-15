package davidnba.com.davidnba_ywh.http.bean.news;


import davidnba.com.davidnba_ywh.http.bean.base.Base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */
public class NewsIndex extends Base {


    /**
     * type : news
     * id : 20160603042788
     * column : banner
     * needUpdate : 0
     */

    public List<IndexBean> data;

    public static class IndexBean implements Serializable {
        public String type;
        public String id;
        public String column;
        public String needUpdate;
    }
}
