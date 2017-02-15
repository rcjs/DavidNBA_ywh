package davidnba.com.davidnba_ywh.http.okhttp;

import com.yuyh.library.utils.log.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 仁昌居士 on 2017/2/9.
 */

public class OkHttpHelper {

    /**
     * 自定义日志输出
     */
    static class MyLog implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogUtils.i("oklog: " + message);
        }
    }

    /**
     * 配置 OKHTTP 拦截器
     *
     * @return
     */
    public static OkHttpClient getHupuClient() {
    CookieInterceptor mCookieInterceptor = new CookieInterceptor();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new MyLog());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(mCookieInterceptor)
                .addInterceptor(logging);
        return builder.build();
    }

    public static OkHttpClient getTecentClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new MyLog());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(logging)
                .addInterceptor(new CommonParamsInterceptor());
        return builder.build();
    }

}
