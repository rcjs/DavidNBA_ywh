package android.davidnba.com.davidnba_ywh.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;

import static android.R.attr.textColor;

/**
 * Created by 仁昌居士 on 2017/2/10.
 */

public class CalendarView extends View implements View.OnTouchListener, ICalendarView {

    private int selectedYear;
    private int selectedMonth;
    private Calendar calendar;

    private int screenWidth;//手机屏幕宽度

    //用来渲染该控件的参数
    private float cellWidth;//一周的每一天的小控件的宽度
    private float cellHeight;//一周的每一天的小控件的高度
    private String[] weekText;
    private int TextColor = CalConstant.TEXT_COLOR;
    private int backgroundColor = CalConstant.BACKGROUND_COLOR;
    private Paint textPaint;
    private Paint weekTextPaint;
    private Paint todayBgPaint;
    private Paint selectedDayBgPaint;
    private Paint selectedDayTextPaint;

    /**
     * the index in date[] of the first day of current month
     * 当月第一天的在周期七天里的位置标签
     */
    private int curStartIndex;

    /**
     * the index in date[] of the last day of current month
     * 当月最后一天的在周期七天里的位置标签
     */
    private int curEndIndex;

    /**
     * the index in date[] of today
     * 今天的位置
     */
    private int todayIndex = -1;


    /**
     * the calendar needs a 6*7 matrix to store
     * date[i] represents the day of position i
     * <p>
     * 42个框子。date[i] 代表i日子
     */
    private int[] date = new int[42];

    /**
     * record the selected index in date[]
     * 记录42个格子里，被选择了的日子所在的位置
     */
    private int selectedIndex = -1;

    /**
     * record the index in date[] of the last ACTION_DOWN event
     * 按下的下标
     */
    private int actionDownIndex = -1;

    private OnItemClickListener onItemClickListener;

    private OnRefreshListener onRefreshListener;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //设置调整到实际的当月
        calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置今日为当月的1号

        //获取屏幕的宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        //设置一周的每一天的小控件的宽度为屏幕宽度的7分之1
        cellWidth = screenWidth / 7f;
        cellHeight = cellWidth * 0.7f;

        //设置背景色
        setBackgroundColor(backgroundColor);

        //设置一周的周名的写法样式
        weekText = getResources().getStringArray(CalConstant.WEEK_TEXT[0]);
        setOnTouchListener(this);

        //设置一周的每一天的小控件里的文字样式
        textPaint = RenderUtil.getPaint(textColor);
        textPaint.setTextSize(cellHeight * 0.4f);

        //设置一周的周名的文字样式
        weekTextPaint = RenderUtil.getPaint(textColor);
        weekTextPaint.setTextSize(cellHeight * 0.4f);
        weekTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //设置今天的样式
        todayBgPaint = RenderUtil.getPaint(Color.parseColor("#FF5055"));
        todayBgPaint.setStrokeWidth(3);
        todayBgPaint.setStyle(Paint.Style.STROKE);//描边

        initial();
    }

    /**
     * calculate the values of date[] and the legal range of index of date[]
     */
    private void initial() {
        //dayOfWeek指当月的第一天是这周的第几天，（周日是1,周一是2）
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int monthStart = -1;
        //dayOfWeek是2~7，即周一到周六，减2为在数组中的位置，原本是减一，
        // 但由于数组下标从0开始，所以，要减二
        if (dayOfWeek >= 2 && dayOfWeek <= 7) {
            monthStart = dayOfWeek - 2;
        } else if (dayOfWeek == 1) {
            monthStart = 6;
        }

        curStartIndex = monthStart;
        date[monthStart] = 1;
        int daysOfMonth = daysOfCurrentMonth();
        for (int i = 1; i < daysOfMonth; i++) {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + daysOfMonth;
        //the year and month selected is the current year and month
        if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
            todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + monthStart - 1;
        } else {
            todayIndex = -1;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight(), MeasureSpec.EXACTLY);
        /*需要注意的是，在setMeasuredDimension()方法调用之后，
        我们才能使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量出的宽高，以此之前调用这两个方法得到的值都会是0。*/
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //计算控件的实际高度
    private int measureHeight() {
        //dayOfWeek指当月的第一天是这周的第几天，（周日是1,周一是2）
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //当月的日子数量
        int daysOfMonth = daysOfCurrentMonth();

        int n = -1;
        if (dayOfWeek >= 2 && dayOfWeek <= 7) {
            n = daysOfMonth - (8 - dayOfWeek + 1);
        } else if (dayOfWeek == 1) {
            n = daysOfMonth - 1;
        }
        int lines = 2 + n / 7 + (n % 7 == 0 ? 0 : 1);//行数
        return (int) (cellHeight * lines);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * render the head
         * 渲染头部一个周期的文字
         */
        float baseline = RenderUtil.getBaseline(0, cellHeight, weekTextPaint);
        for (int i = 0; i < 7; i++) {
            //获取x轴上要让文字居中的起始距离
            float weekTextX = RenderUtil.getStartX(cellWidth * i + cellWidth * 0.5f, weekTextPaint, weekText[i]);
            //画文字
            canvas.drawText(weekText[i], weekTextX, baseline, weekTextPaint);
        }
        for (int i = curStartIndex; i < curEndIndex; i++) {
            if (i == todayIndex && i == selectedIndex) {
                //画红色圈圈，圈出当天(填充式)
                drawCircle(canvas, i, selectedDayBgPaint, cellHeight + 0.48f);
                //画文字
                drawText(canvas, i, selectedDayTextPaint, "" + date[i]);
            } else if (i == todayIndex) {
                //画红色圈圈，圈出当天(描边式)
                drawCircle(canvas, i, todayBgPaint, cellHeight * 0.48f);
                //画文字
                drawText(canvas, i, textPaint, "" + date[i]);
            } else if (i == selectedIndex) {
                //画红色圈圈，圈出当天(填充式)
                drawCircle(canvas, i, selectedDayBgPaint, cellHeight * 0.48f);
                //画文字
                drawText(canvas, i, textPaint, "" + date[i]);
            }
        }
    }

    private void drawText(Canvas canvas, int index, Paint paint, String text) {
        if (isIllegalIndex(index)) {
            return;
        }
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float top = cellHeight + (y - 1) * cellHeight;
        float bottom = top + cellHeight;
        float baseline = RenderUtil.getBaseline(top, bottom, paint);
        float startX = RenderUtil.getStartX(cellWidth * (x - 1) + cellWidth * 0.5f, paint, text);
        canvas.drawText(text, startX, baseline, paint);
    }

    private int getYByIndex(int index) {
        return index / 7 + 1;
    }

    private int getXByIndex(int index) {
        return index % 7 + 1;
    }

    //判断这个下标是否违法、越界
    private boolean isIllegalIndex(int index) {
        return index < curStartIndex || index >= curEndIndex;
    }

    private void drawCircle(Canvas canvas, int index, Paint paint, float radius) {
        if (isIllegalIndex(index)) {
            return;
        }
        int x = getXByIndex(index);
        int y = getXByIndex(index);
        float centerX = cellWidth * (x - 1) + cellWidth * 0.5f;
        float centerY = cellHeight + (y - 1) * cellHeight + cellHeight * 0.5f;//算上加一行周期的文字
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    private static int leap(int year) {
        return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? 1 : 0;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (coordIsCalendarCell(y)) {
                    int index = getIndexByCoordinate(x, y);
                    if (isLegalIndex(index)) {
                        actionDownIndex = index;
                        selectedIndex = -1;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (coordIsCalendarCell(y)) {
                    int actionUpIndex = getIndexByCoordinate(x, y);
                    if (isLegalIndex(actionUpIndex)) {
                        if (actionDownIndex == actionUpIndex) {
                            selectedIndex = actionUpIndex;
                            actionDownIndex = -1;
                            int day = date[actionUpIndex];
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(day);
                            }
                            invalidate();
                        }
                    }
                }
                break;
        }
        return true;
    }

    /**
     * whether the index is legal
     *
     * @param i the index in date[]
     * @return
     */
    private boolean isLegalIndex(int i) {
        return !isIllegalIndex(i);
    }

    /**
     * calculate the index of date[] according to the coordination
     * 根据选择的位置确定点击是哪个下标
     */
    private int getIndexByCoordinate(float x, float y) {
        int m = (int) (Math.floor(x / cellWidth) + 1);
        int n = (int) (Math.floor((y - cellHeight) / cellHeight) + 1);
        return (n - 1) * 7 + m - 1;
    }

    /**
     * y is bigger than the head of the calendar, meaning that the coordination may represent a day of the calendar
     */
    private boolean coordIsCalendarCell(float y) {
        return y > cellHeight;
    }


    @Override
    public int daysOfCurrentMonth() {
        return CalConstant.DAYS_OF_MONTH[leap(selectedYear)][selectedMonth];
    }

    @Override
    public Calendar getCalendar() {
        return null;
    }

    @Override
    public void refresh(int year, int month) {

    }

    @Override
    public void setSelectedDayTextColor(int color) {

    }

    @Override
    public void setSelectedDayBgColor(int color) {

    }

    @Override
    public void setTodayBgColor(int color) {

    }

    @Override
    public int daysCompleteTheTask() {
        return 0;
    }

    @Override
    public void setWeekTextStyle(int style) {

    }

    @Override
    public void setWeekTextColor(int color) {

    }

    @Override
    public void setCalendarTextColor(int color) {

    }

    @Override
    public void setWeekTextSizeScale(float scale) {

    }

    @Override
    public void setTextSizeScale(float scale) {

    }

    @Override
    public int getYear() {
        return 0;
    }

    @Override
    public int getMonth() {
        return 0;
    }

}
