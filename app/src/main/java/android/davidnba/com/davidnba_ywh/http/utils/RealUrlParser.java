package android.davidnba.com.davidnba_ywh.http.utils;


import android.davidnba.com.davidnba_ywh.http.bean.news.VideoRealUrl;

import java.io.InputStream;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */
public interface RealUrlParser {

    VideoRealUrl parse(InputStream is) throws Exception;
}
