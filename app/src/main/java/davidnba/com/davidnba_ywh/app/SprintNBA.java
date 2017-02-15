package davidnba.com.davidnba_ywh.app;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.yuyh.library.AppUtils;
import com.yuyh.library.CrashHandler;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by 仁昌居士 on 2017/2/6.
 */

public class SprintNBA extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppUtils.init(mContext);
    //    CrashHandler.getInstance().init(this);
        initBomb();
        initFresco();
    }

    private void initFresco() {
     /* 图片的渐进式加载（网速有点快，如果仔细看的话，是可以看出来的）：
        Fresco 支持渐进式的网络JPEG图。在开始加载之后，图会从模糊到清晰渐渐呈现。
        你可以设置一个清晰度标准，在未达到这个清晰度之前，会一直显示占位图。
        渐进式JPEG图仅仅支持网络图。*/
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .build();

        //Fresco本身提供了两种初始化方式，一种是使用使用默认配置初始化，另一种是使用用户自定义配置。
        // 如下代码是Fresco提供的两个初始化方法。第一个只需要提供一个Context参数，第二个还需要提供 ImagePipeline 的配置实例 - ImagePipelineConfig。
        Fresco.initialize(this, config);
    }

    private void initBomb() {
BmobConfig config = new BmobConfig.Builder(this)
        .setApplicationId("e47d1b58418a7db9bc0f66c3628f11dc")//设置appkey
        .setConnectTimeout(30)//请求超时时间（单位为秒）：默认15s
        .setUploadBlockSize(1024 *1024)//文件分片上传时每片的大小（单位字节），默认512*1024
        .setFileExpiration(2500)//文件的过期时间（单位为秒）:默认1800s
        .build();
        Bmob.initialize(config);
    }

    public static Context getAppContext(){
        return mContext;
    }
}
