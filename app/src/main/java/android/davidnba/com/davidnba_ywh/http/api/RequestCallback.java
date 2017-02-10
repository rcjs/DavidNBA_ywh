package android.davidnba.com.davidnba_ywh.http.api;

/**
 * Created by 仁昌居士 on 2017/2/9.
 */

public interface RequestCallback<T> {
    void onSuccess(T t);

    void onFailure(String message);
}
