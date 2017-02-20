package davidnba.com.davidnba_ywh.http.bean.match;


import java.util.List;

import davidnba.com.davidnba_ywh.http.bean.base.Base;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */
public class LiveIndex extends Base {

    public Index data;

    public static class Index{
        public List<String> index;
    }
}
