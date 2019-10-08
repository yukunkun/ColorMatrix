package com.matrix.yukun.matrix.selfview.calendar.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.calendar.CalendarUtils;
import com.matrix.yukun.matrix.selfview.calendar.LunarCalendarUtils;
import org.joda.time.DateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 周视图周历
 *
 * @author liufeng
 * @date 2017-7-22
 */
public class WeekView extends View {

    private static final int   NUM_COLUMNS = 7;
    private              Paint mPaint;
    private              Paint mLunarPaint;
    private              Paint mHolidayPaint;
    private              int   mNormalDayColor;
    private              int   mSelectDayColor;
    private              int   mSelectBGColor;
    private              int   mSelectBGTodayColor;
    private              int   mCurrentDayColor;
    private              int   mHintCircleColor;
    private              int   mTodayNotSeleBGColor;
    private              int   mLunarTextColor;
    private              int   mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize, mSelectCircleSize;
    private int                 mDaySize;
    private int                 mLunarTextSize;
    private int                 mCircleRadius = 6;
    private boolean             mIsShowLunar;
    private boolean             mIsShowHint;
    private boolean             mIsShowHolidayHint;
    private DateTime            mStartDate;
    private DisplayMetrics      mDisplayMetrics;
    private OnWeekClickListener mOnWeekClickListener;
    private GestureDetector     mGestureDetector;
    private Bitmap mRestBitmap;
    private Bitmap mWorkBitmap;
    private int[]               mHolidays;
    private String[]            mHolidayOrLunarText;
    private int                 mHolidayTextColor;

    public WeekView(Context context, DateTime dateTime) {
        this(context, null, dateTime);
    }

    public WeekView(Context context, TypedArray array, DateTime dateTime) {
        this(context, array, null, dateTime);
    }

    public WeekView(Context context, TypedArray array, AttributeSet attrs, DateTime dateTime) {
        this(context, array, attrs, 0, dateTime);
    }

    public WeekView(Context context, TypedArray array, AttributeSet attrs, int defStyleAttr, DateTime dateTime) {
        super(context, attrs, defStyleAttr);
        initAttrs(array, dateTime);
        initPaint();
        initWeek();
        initGestureDetector();
    }

    private void initAttrs(TypedArray array, DateTime dateTime) {
        if (array != null) {
            mSelectDayColor = array.getColor(R.styleable.WeekCalendarView_week_selected_text_color, Color.parseColor("#FFFFFF"));
            mSelectBGColor = array.getColor(R.styleable.WeekCalendarView_week_selected_circle_color, Color.parseColor("#9082BD"));
            mSelectBGTodayColor = array.getColor(R.styleable.WeekCalendarView_week_selected_circle_today_color, Color.parseColor("#FA7479"));
            mNormalDayColor = array.getColor(R.styleable.WeekCalendarView_week_normal_text_color, Color.parseColor("#575471"));
            mCurrentDayColor = array.getColor(R.styleable.WeekCalendarView_week_today_text_color, Color.parseColor("#FF8594"));
            mHintCircleColor = array.getColor(R.styleable.WeekCalendarView_week_hint_circle_color, Color.parseColor("#4393f9"));// 提示小圆点颜色
            mLunarTextColor = array.getColor(R.styleable.WeekCalendarView_week_lunar_text_color, Color.parseColor("#ACA9BC"));
            mHolidayTextColor = array.getColor(R.styleable.WeekCalendarView_week_holiday_color, Color.parseColor("#4393f9"));
            mDaySize = array.getInteger(R.styleable.WeekCalendarView_week_day_text_size, 13);
            mLunarTextSize = array.getInteger(R.styleable.WeekCalendarView_week_day_lunar_text_size, 8);
            mIsShowHint = array.getBoolean(R.styleable.WeekCalendarView_week_show_task_hint, true);
            mIsShowLunar = array.getBoolean(R.styleable.WeekCalendarView_week_show_lunar, true);
            mIsShowHolidayHint = array.getBoolean(R.styleable.WeekCalendarView_week_show_holiday_hint, true);
            mTodayNotSeleBGColor = getResources().getColor(R.color.C5);//背景色
        }
        else {
            mSelectDayColor = Color.parseColor("#FFFFFF");
            mSelectBGColor = Color.parseColor("#9082BD");
            mSelectBGTodayColor = Color.parseColor("#FF8594");
            mNormalDayColor = Color.parseColor("#575471");
            mCurrentDayColor = Color.parseColor("#FF8594");
            mHintCircleColor = Color.parseColor("#FE8595");
            mLunarTextColor = Color.parseColor("#ACA9BC");
            mHolidayTextColor = Color.parseColor("#4393f9");
            mTodayNotSeleBGColor = getResources().getColor(R.color.C5);//背景色
            mDaySize = 13;
            mDaySize = 8;
            mIsShowHint = true;
            mIsShowLunar = true;
        }
        mStartDate = dateTime;
        mRestBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rest_day);
        mWorkBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_work_day);
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);

        mLunarPaint = new Paint();
        mLunarPaint.setAntiAlias(true);
        mLunarPaint.setTextSize(mLunarTextSize * mDisplayMetrics.scaledDensity);
        mLunarPaint.setColor(mLunarTextColor);

        mHolidayPaint = new Paint();
        mHolidayPaint.setAntiAlias(true);
        mHolidayPaint.setTextSize(mLunarTextSize * mDisplayMetrics.scaledDensity);
    }

    private void initWeek() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        DateTime endDate = mStartDate.plusDays(7);
        if (mStartDate.getMillis() <= System.currentTimeMillis() && endDate.getMillis() > System.currentTimeMillis()) {
            if (mStartDate.getMonthOfYear() != endDate.getMonthOfYear()) {
                if (mCurrDay < mStartDate.getDayOfMonth()) {
                    setSelectYearMonth(mStartDate.getYear(), endDate.getMonthOfYear() - 1, mCurrDay);
                }
                else {
                    setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mCurrDay);
                }
            }
            else {
                setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mCurrDay);
            }
        }
        else {
            setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mStartDate.getDayOfMonth());
        }
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        clearData();
        int selected = drawThisWeek(canvas);
        drawLunarText(canvas, selected);
        drawHintCircle(canvas);
        drawHoliday(canvas);
    }

    private void clearData() {
        mHolidayOrLunarText = new String[7];
    }

    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight();
        mSelectCircleSize = (int) (mColumnSize / 3.2);
        while (mSelectCircleSize > mRowSize / 2) {
            mSelectCircleSize = (int) (mSelectCircleSize / 1.3);
        }
    }

    private int drawThisWeek(Canvas canvas) {
        int selected = 0;
        for (int i = 0; i < 7; i++) {
            DateTime date = mStartDate.plusDays(i);
            int day = date.getDayOfMonth();
            String dayString = String.valueOf(day);
            int startX = (int) (mColumnSize * i + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            //拿到这个时间属于星期几，星期六和星期日要改成红色
            String dayWeek = getWeekOfDate(date.toDate());
            if (day == mSelDay) {
                int startRecX = mColumnSize * i;
                int endRecX = startRecX + mColumnSize;
                if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay) {
                    mPaint.setColor(mSelectBGTodayColor);
                }
                else {
                    mPaint.setColor(mSelectBGColor);
                }
                RectF r2 = new RectF();
                r2.left = ((startRecX + endRecX) / 2) - (mSelectCircleSize + 4);
                r2.right = ((startRecX + endRecX) / 2) + (mSelectCircleSize + 4);
                r2.top = (mRowSize / 2 - (mSelectCircleSize - 4));
                r2.bottom = (mRowSize - 4);
//                canvas.drawRoundRect(r2, 10, 10, mPaint);//第二个参数是x半径，第三个参数是y半径
                canvas.drawCircle((startRecX + endRecX) / 2, mRowSize / 2, mSelectCircleSize, mPaint);
            }
            if (day == mSelDay) {
                selected = i;
                mPaint.setColor(mSelectDayColor);
            }
            else if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay && day != mSelDay && mCurrYear == mSelYear) {
                mPaint.setColor(mCurrentDayColor);
            }
            else if (dayWeek.equals("星期六") || dayWeek.equals("星期日")) {
                mPaint.setColor(Color.parseColor("#FF6D6E"));
            }
            else {
                mPaint.setColor(mNormalDayColor);
            }
            // 当前天
            if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay) {
                int startRecX = mColumnSize * i;
                int endRecX = startRecX + mColumnSize;
                if (day == mSelDay) {
                    mPaint.setColor(mSelectBGTodayColor);
                }else {
                    mPaint.setColor(mTodayNotSeleBGColor);
                }
                RectF r2 = new RectF();
                r2.left = ((startRecX + endRecX) / 2) - (mSelectCircleSize + 4);
                r2.right = ((startRecX + endRecX) / 2) + (mSelectCircleSize + 4);
                r2.top = (mRowSize / 2 - (mSelectCircleSize - 4));
                r2.bottom = (mRowSize - 4);
//                canvas.drawRoundRect(r2, 10, 10, mPaint);//第二个参数是x半径，第三个参数是y半径
                canvas.drawCircle((startRecX + endRecX) / 2, mRowSize / 2, mSelectCircleSize, mPaint);
                mPaint.setColor(mSelectDayColor);//设置今天字体为白色
            }
            canvas.drawText(dayString, startX, startY - 12, mPaint);
            mHolidayOrLunarText[i] = CalendarUtils.getHolidayFromSolar(date.getYear(), date.getMonthOfYear() - 1, day);
        }
        return selected;
    }

    public static String getWeekOfDate(Date date) {
        String[] weekArray = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekIndex < 0) {
            weekIndex = 0;
        }
        return weekArray[weekIndex];
    }
    /**
     * 绘制农历
     *
     * @param canvas
     * @param selected
     */
    private void drawLunarText(Canvas canvas, int selected) {
        if (mIsShowLunar) {
            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(mStartDate.getYear(), mStartDate.getMonthOfYear(), mStartDate.getDayOfMonth()));
            int leapMonth = LunarCalendarUtils.leapMonth(lunar.lunarYear);
            int days = LunarCalendarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
            int day = lunar.lunarDay;
            boolean isMonthInitialize = false;//月份是否初始化过，因为下面判断当前月份非闰月时 会递增，在获取节假日的时候 就拿不到一月的节假日，所以需要多一个判断
            for (int i = 0; i < 7; i++) {
                DateTime date = mStartDate.plusDays(i);
                int monthDay = date.getDayOfMonth();
                if (day > days) {
                    day = 1;
                    if (lunar.lunarMonth == 12) {
                        lunar.lunarMonth = 1;
                        isMonthInitialize = true;
                        lunar.lunarYear = lunar.lunarYear + 1;
                    }
                    if (lunar.lunarMonth == leapMonth) {
                        days = LunarCalendarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
                    }
                    else {
                        lunar.lunarMonth++;
                        days = LunarCalendarUtils.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
                    }
                }
                mLunarPaint.setColor(mHolidayTextColor);
                String dayString = mHolidayOrLunarText[i];
                if (TextUtils.isEmpty(dayString)) {
                    dayString = "";
                }
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getLunarHoliday(lunar.lunarYear, isMonthInitialize ? 1 : lunar.lunarMonth, day);
                }

                String tempDayString = "";//用作节气缓存的数据
                if (!TextUtils.isEmpty(dayString)) {
                    tempDayString = dayString;
                }
                if (TextUtils.isEmpty(dayString) || !dayString.contains("节")) {
                    dayString = LunarCalendarUtils.getLunarHoliday(lunar.lunarYear, isMonthInitialize ? 1 : lunar.lunarMonth, day);
                    if (TextUtils.isEmpty(dayString)) {
                        dayString = tempDayString;
                    }
                }
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getLunarDayString(day);
                    if (dayString.equals("初一")) {
                        dayString = lunar.lunarMonth + "月";
                    }
                    mLunarPaint.setColor(mLunarTextColor);
                }
                if (i == selected) {
                    mLunarPaint.setColor(mSelectDayColor);
                }
                if (mCurrDay == monthDay) {
                    mLunarPaint.setColor(mSelectDayColor);
                }
                int startX = (int) (mColumnSize * i + (mColumnSize - mLunarPaint.measureText(dayString)) / 2);
                int startY = (int) (mRowSize * 0.72 - (mLunarPaint.ascent() + mLunarPaint.descent()) / 2);
                canvas.drawText(dayString, startX, startY - 4, mLunarPaint);
                day++;
            }
        }
    }

    /**
     * 绘制节假日
     *
     * @param canvas
     */
    private void drawHoliday(Canvas canvas) {
        int holidays[] = CalendarUtils.getInstance(getContext()).getHolidays(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1);
        int row = CalendarUtils.getWeekRow(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mStartDate.getDayOfMonth());
        mHolidays = new int[7];
        System.arraycopy(holidays, row * 7, mHolidays, 0, mHolidays.length);
        if (mIsShowHolidayHint) {
            int distance = (int) (mSelectCircleSize / 2.5);
            for (int i = 0; i < mHolidays.length; i++) {
                int column = i % 7;
                int startX = (int) (mColumnSize * column + (mColumnSize - mLunarPaint.measureText("休")) / 1.2);
                int startY = (int) (mRowSize / 2.1 - (mLunarPaint.ascent() + distance) / 2);
                if (mHolidays[i] == 1) {
                    mHolidayPaint.setColor(Color.parseColor("#FFFF6D6E"));
                    canvas.drawText("休", startX - 8, startY - 12, mHolidayPaint);
                }
                else if (mHolidays[i] == 2) {
                    mHolidayPaint.setColor(Color.parseColor("#FF2B2C32"));
                    canvas.drawText("班", startX - 8, startY - 12, mHolidayPaint);
                }
            }
        }
    }

    /**
     * 绘制圆点提示
     *
     * @param canvas
     */
    private void drawHintCircle(Canvas canvas) {
        if (mIsShowHint) {
            mPaint.setColor(mHintCircleColor);
            int startMonth = mStartDate.getMonthOfYear();
            int endMonth = mStartDate.plusDays(7).getMonthOfYear();
            int startDay = mStartDate.getDayOfMonth();
            int whiteColor = Color.parseColor("#FFFFFF");
            if (startMonth == endMonth) {
                Set<Integer> hints = (Set<Integer>) CalendarUtils.getInstance(getContext()).getTaskHints(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1);
                for (int i = 0; i < 7; i++) {
                    mPaint.setColor((mSelMonth == mCurrMonth && startDay + i == mCurrDay) || startDay + i == mSelDay ? whiteColor : mHintCircleColor);
                    drawHintCircle(hints, startDay + i, i, canvas);
                }
            }
            else {
                for (int i = 0; i < 7; i++) {
                    Set<Integer> hints = (Set<Integer>) CalendarUtils.getInstance(getContext()).getTaskHints(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1);
                    Set<Integer> nextHints = (Set<Integer>) CalendarUtils.getInstance(getContext()).getTaskHints(mStartDate.getYear(), mStartDate.getMonthOfYear());
                    DateTime date = mStartDate.plusDays(i);
                    int month = date.getMonthOfYear();
                    int nextDay = date.getDayOfMonth();
                    mPaint.setColor(month - 1 == mCurrMonth && nextDay == mCurrDay || nextDay == mSelDay ? whiteColor : mHintCircleColor);
                    if (month == startMonth) {
                        drawHintCircle(hints, nextDay, i, canvas);
                    }
                    else {
                        drawHintCircle(nextHints, nextDay, i, canvas);
                    }
                }
            }
        }
    }

    private void drawHintCircle(Set<Integer> hints, int day, int col, Canvas canvas) {
        if (!hints.contains(day)) {
            return;
        }
        float circleX = (float) (mColumnSize * col + mColumnSize * 0.5);
        float circleY = (float) (mRowSize * 0.87);//加农历就是0.87，不加农历就是0.75
        canvas.drawCircle(circleX, circleY, mCircleRadius, mPaint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void doClickAction(int x, int y) {
        if (y > getHeight()) {
            return;
        }
        int column = x / mColumnSize;
        column = Math.min(column, 6);
        DateTime date = mStartDate.plusDays(column);
        clickThisWeek(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    public void clickThisWeek(int year, int month, int day) {
        if (mOnWeekClickListener != null) {
            mOnWeekClickListener.onClickDate(year, month, day);
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    public void setOnWeekClickListener(OnWeekClickListener onWeekClickListener) {
        mOnWeekClickListener = onWeekClickListener;
    }

    public DateTime getStartDate() {
        return mStartDate;
    }

    public DateTime getEndDate() {
        return mStartDate.plusDays(6);
    }

    /**
     * 获取当前选择年
     *
     * @return
     */
    public int getSelectYear() {
        return mSelYear;
    }

    /**
     * 获取当前选择月
     *
     * @return
     */
    public int getSelectMonth() {
        return mSelMonth;
    }

    /**
     * 获取当前选择日
     *
     * @return
     */
    public int getSelectDay() {
        return this.mSelDay;
    }

    /**
     * 添加多个圆点提示
     *
     * @param hints
     */
    public void addTaskHints(List<Integer> hints) {
        if (mIsShowHint) {
            CalendarUtils.getInstance(getContext()).addTaskHints(mSelYear, mSelMonth, hints);
            invalidate();
        }
    }

    /**
     * 删除多个圆点提示
     *
     * @param hints
     */
    public void removeTaskHints(List<Integer> hints) {
        if (mIsShowHint) {
            CalendarUtils.getInstance(getContext()).removeTaskHints(mSelYear, mSelMonth, hints);
            invalidate();
        }
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    public void addTaskHint(Integer day) {
        if (mIsShowHint) {
            if (CalendarUtils.getInstance(getContext()).addTaskHint(mSelYear, mSelMonth, day)) {
                invalidate();
            }
        }
    }

    /**
     * 删除一个圆点提示
     *
     * @param day
     */
    public void removeTaskHint(Integer day) {
        if (mIsShowHint) {
            if (CalendarUtils.getInstance(getContext()).removeTaskHint(mSelYear, mSelMonth, day)) {
                invalidate();
            }
        }
    }
}
