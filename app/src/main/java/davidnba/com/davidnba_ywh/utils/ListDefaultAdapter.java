package davidnba.com.davidnba_ywh.utils;

import davidnba.com.davidnba_ywh.http.bean.match.MatchStat;
import davidnba.com.davidnba_ywh.http.utils.MatchPlayerInfoDefaultAdapter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 仁昌居士 on 2017/2/9.
 * <p>
 * 处理MatchStat里面出现key一样，但是value有时为对象，有时为List引起的问题（腾讯NBA的Json太多坑了）
 */

public class ListDefaultAdapter implements JsonDeserializer<List<?>> {

    @Override
    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            Gson newGson = new GsonBuilder().serializeNulls()
                    .registerTypeAdapter(MatchStat.MatchStatInfo.StatsBean.MaxPlayers.MatchPlayerInfo.class,
                            new MatchPlayerInfoDefaultAdapter())
                    .create();
            if (json.isJsonArray()) {
                return newGson.fromJson(json, typeOfT);
            } else if (json.getAsString() != null && !json.getAsString().equals("")) {
                String newjson = "[" + json.getAsString() + "]";
                return newGson.fromJson(newjson, typeOfT);
            }
        } catch (Exception ignore) {
            Log.e("---", ignore.toString());
        }
        return null;
    }
}
