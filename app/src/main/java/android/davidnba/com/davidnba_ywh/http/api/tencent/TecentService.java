package android.davidnba.com.davidnba_ywh.http.api.tencent;


import android.davidnba.com.davidnba_ywh.BuildConfig;
import android.davidnba.com.davidnba_ywh.http.api.RequestCallback;
import android.davidnba.com.davidnba_ywh.http.bean.match.MatchCalendar;
import android.davidnba.com.davidnba_ywh.http.okhttp.OkHttpHelper;
import android.davidnba.com.davidnba_ywh.http.utils.JsonParser;
import android.text.TextUtils;

import com.yuyh.library.AppUtils;
import com.yuyh.library.utils.data.ACache;
import com.yuyh.library.utils.log.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by 仁昌居士 on 2017/2/8.
 */

public class TecentService {

    /*
   https://gold.xitu.io/entry/5729cae2f38c840067907451
   1. 返回原始的的json
    场景是这样，服务端返回一个不定类型的json数据，无法确定映射的Gson对象。
    或者本猿就是要看原汁原味的json肿么办？我依旧使用GsonConverterFactory作为转换器，返回的对象定义为String。。
    结果不行。。。各种研究+求教后得到解决方案。应该使用ScalarsConverterFactory*/
    static Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.TENCENT_SERVER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(OkHttpHelper.getTecentClient()).build();

    static TencentApi api = retrofit.create(TencentApi.class);


    /**
     * 根据球队Id及年份月份获取赛程
     *
     * @param teamId    球队ID，默认-1为查询所有
     * @param year      年份
     * @param month     月份
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */
    public static void getMatchCalendar(int teamId, int year, int month, boolean isRefresh, final RequestCallback<MatchCalendar> cbk) {
        final String key = "getMatchCalendar" + teamId + year + month;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            MatchCalendar match = (MatchCalendar) obj;
            cbk.onSuccess(match);
            return;
        }

        Call<String> call = api.getMatchCalendar(teamId, year, month);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response != null && TextUtils.isEmpty(response.body())){
                    String jsonStr = response.body();
                    MatchCalendar match = JsonParser.parseWithGson(MatchCalendar.class,jsonStr);
                    cbk.onSuccess(match);
                    cache.put(key,match);
                    LogUtils.i("resp:" + jsonStr);
                }else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }


}
