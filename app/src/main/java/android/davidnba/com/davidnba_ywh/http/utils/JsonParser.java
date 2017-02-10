package android.davidnba.com.davidnba_ywh.http.utils;

import android.davidnba.com.davidnba_ywh.http.bean.base.Base;
import android.davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import android.davidnba.com.davidnba_ywh.utils.ListDefaultAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by 仁昌居士 on 2017/2/9.
 */

public class JsonParser {
    static Gson gson = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(MatchStat.MatchStatInfo.StatsBean.MaxPlayers.MatchPlayerInfo.class, new MatchPlayerInfoDefaultAdapter())
            .registerTypeHierarchyAdapter(List.class, new ListDefaultAdapter())
            .create();

    public static String parseBase(Base base, String jsonStr) {
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        String data = "{}";
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            if (entry.getKey().equals("code")) {
                base.code = Integer.parseInt(entry.getValue().toString());
            } else if (entry.getKey().equals("version")) {
                base.version = entry.getValue().toString();
            }else {
                data = entry.getValue().toString();
            }
        }
        return data;
    }

    public static <T> T parseWithGson(Class<T> classOfT,String jsonStr){
        return gson.fromJson(jsonStr,classOfT);
    }



















}
