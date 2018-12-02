package com.homework.ksing.ksing.ui.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.homework.ksing.ksing.R;

/**
 * 简书ID：天哥在奔跑
 * 原创Android教程：http://www.jianshu.com/p/9618c038135f
 * 教程答疑专用QQ群：667833258
 */
public class NavIndicator extends View {

    private int pageCount;
    private int pageSelected;
    private int width = getResources().getDimensionPixelSize(R.dimen.indicator_width);
    private int height = getResources().getDimensionPixelSize(R.dimen.indicator_height);
    private int gap = getResources().getDimensionPixelSize(R.dimen.indicator_gap);
    private Paint mPaint;
    private String focusColor = "#FFFFFFFF";
    private String unfocusColor = "#99FFFFFF";

    public NavIndicator(Context context) {
        super(context);
        init();
    }

    public NavIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(), measureHeight());
    }

    protected int measureWidth() {
        return width * pageCount + gap * (pageCount - 1);
    }

    protected int measureHeight() {
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        if (pageCount == 1) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(focusColor));
            Rect rect = new Rect(0, 0, width, height);
            canvas.drawRect(rect, mPaint);
        } else {
            for (int i = 0; i < pageCount; i++) {
                if (i == pageSelected) {
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(Color.parseColor(focusColor));
                    Rect rect = new Rect((width + gap) * i, 0, width + i * (width + gap), height);
                    canvas.drawRect(rect, mPaint);
                } else {
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(Color.parseColor(unfocusColor));
                    Rect rect = new Rect((width + gap) * i, 0, width + i * (width + gap), height);
                    canvas.drawRect(rect, mPaint);
                }
            }
        }
    }

    public void setIndicatorColor(String focusColor, String unfocusColor) {
        this.focusColor = focusColor;
        this.unfocusColor = unfocusColor;
    }

    public void setPageCount(int count) {
        this.pageCount = count;
        this.requestLayout();
    }

    public void onPageSelected(int index) {
        this.pageSelected = index;
        this.postInvalidate();
    }
}
