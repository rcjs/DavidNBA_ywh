package davidnba.com.davidnba_ywh.http.api.tencent;


import davidnba.com.davidnba_ywh.BuildConfig;
import davidnba.com.davidnba_ywh.app.Constant;
import davidnba.com.davidnba_ywh.http.api.RequestCallback;
import davidnba.com.davidnba_ywh.http.bean.match.MatchBaseInfo;
import davidnba.com.davidnba_ywh.http.bean.match.MatchCalendar;
import davidnba.com.davidnba_ywh.http.bean.match.Matchs;
import davidnba.com.davidnba_ywh.http.bean.news.NewsDetail;
import davidnba.com.davidnba_ywh.http.bean.news.NewsIndex;
import davidnba.com.davidnba_ywh.http.bean.news.NewsItem;
import davidnba.com.davidnba_ywh.http.bean.news.VideoRealUrl;
import davidnba.com.davidnba_ywh.http.okhttp.OkHttpHelper;
import davidnba.com.davidnba_ywh.http.utils.JsonParser;
import davidnba.com.davidnba_ywh.http.utils.PullRealUrlParser;
import android.text.TextUtils;

import com.yuyh.library.AppUtils;
import com.yuyh.library.utils.data.ACache;
import com.yuyh.library.utils.log.LogUtils;

import java.io.ByteArrayInputStream;

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



    public static void getVideoRealUrl(String vid,final RequestCallback<VideoRealUrl> cbk){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.TECENT_URL_SERVER)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(OkHttpHelper.getTecentClient()).build();
        TencentVideoApi api = retrofit.create(TencentVideoApi.class);
        Call<String> call = api.getVideoRealUrl(vid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String xmlStr = response.body();
                    PullRealUrlParser parser = new PullRealUrlParser();
                    try {
                        VideoRealUrl url = parser.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
                        cbk.onSuccess(url);
                    } catch (Exception e) {
                        LogUtils.e("解析xml异常:" + e.getMessage());
                        cbk.onFailure("解析出错");
                    }
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogUtils.e(t.getMessage());
                cbk.onFailure(t.getMessage());
            }
        });
    }


    /**
     * 获取新闻的详细内容
     *
     * @param newsType  文章类型
     * @param articleId 文章id
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */
    public static void getNewsDetail(Constant.NewsType newsType, String articleId, boolean isRefresh, final RequestCallback<NewsDetail> cbk) {
        final String key = "getNewsDetail" + articleId;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsDetail detail = (NewsDetail) obj;
            cbk.onSuccess(detail);
            return;
        }

        Call<String> call = api.getNewsDetail(newsType.getType(), articleId);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsDetail detail = JsonParser.parseNewsDetail(jsonStr);
                    cbk.onSuccess(detail);
                    cache.put(key, detail);
                    LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 获取所有新闻索引
     *
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */
    public static void getNewsIndex(Constant.NewsType newsType, boolean isRefresh, final RequestCallback<NewsIndex> cbk) {
        final String key = "getNewsIndex" + newsType.getType();
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsIndex index = (NewsIndex) obj;
            cbk.onSuccess(index);
            return;
        }

        Call<String> call = api.getNewsIndex(newsType.getType());
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsIndex index = JsonParser.parseWithGson(NewsIndex.class, jsonStr);
                    cbk.onSuccess(index);
                    cache.put(key, index);
                    LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 根据索引获取新闻列表
     *
     * @param articleIds 索引值。多个索引以“,”分隔
     * @param isRefresh  是否重新请求数据
     * @param cbk
     */
    public static void getNewsItem(Constant.NewsType newsType, String articleIds, boolean isRefresh, final RequestCallback<NewsItem> cbk) {
        final String key = "getNewsItem" + articleIds;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsItem newsItem = (NewsItem) obj;
            cbk.onSuccess(newsItem);
            return;
        }

        Call<String> call = api.getNewsItem(newsType.getType(), articleIds);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsItem newsItem = JsonParser.parseNewsItem(jsonStr);
                    cbk.onSuccess(newsItem);
                    cache.put(key, newsItem);
                    LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }


    /**
     * 根据日期查询赛程
     *
     * @param date      日期。格式：YYYY-MM-DD
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */
    public static void getMatchsByDate(String date, boolean isRefresh, final RequestCallback<Matchs> cbk) {
        final String key = "getMatchsByDate" + date;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            Matchs matchs = (Matchs) obj;
            cbk.onSuccess(matchs);
            return;
        }

        Call<String> call = api.getMatchsByData(date);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    LogUtils.d("resp:" + jsonStr);
                    Matchs matchs = JsonParser.parseWithGson(Matchs.class, jsonStr);
                    cbk.onSuccess(matchs);
                    cache.put(key, matchs);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 获取比赛信息
     *
     * @param mid
     * @param cbk
     */
    public static void getMatchBaseInfo(String mid, final RequestCallback<MatchBaseInfo.BaseInfo> cbk) {
        Call<String> call = api.getMatchBaseInfo(mid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    MatchBaseInfo info = JsonParser.parseWithGson(MatchBaseInfo.class, jsonStr);
                    cbk.onSuccess(info.data);
                    LogUtils.d("resp:" + jsonStr);
                } else {
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
