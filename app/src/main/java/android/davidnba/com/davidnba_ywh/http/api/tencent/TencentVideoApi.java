package android.davidnba.com.davidnba_ywh.http.api.tencent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 仁昌居士 on 2017/2/13.
 */

public interface TencentVideoApi {
    @GET("/getinfo?otype=xml&platform=1&ran=0%2E9652906153351068")
    Call<String> getVideoRealUrl(@Query("vid") String vid);
}
