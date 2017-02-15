package davidnba.com.davidnba_ywh.widget.calendar;

import android.graphics.Paint;

/**
 * Created by 仁昌居士 on 2017/2/10.
 */

//画笔的工具类
public class RenderUtil {

    //获取TextView的中心基准线
    public static float getBaseline(float top, float bottom, Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (top + bottom - fontMetrics.bottom -fontMetrics.top) /2;
    }

    //获取x轴上要让文字居中的起始距离
    public static float getStartX(float middle,Paint paint,String text){
        //子控件的中心点减去 paint.measureText(text)测量文本的宽度
        return middle - paint.measureText(text) * 0.5f;
    }

    //设置画笔工具，（抗锯齿）
    public static Paint getPaint(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);//防锯齿
        return paint;
    }
}
