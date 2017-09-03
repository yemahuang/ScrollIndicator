package com.lezhi.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lezhi on 2017/9/3.
 */

public class IndicatorTextView extends TextView {

    private Paint mPaint;
    private Rect mTextRect;
    private RectF mRect;
    private int mSelectedTextColor;
    private int mNormalTextColor;
    private int mDrawSelectedTextColor;
    private int mDrawNormalTextColor;

    public IndicatorTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorTextView);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalTextColor = typedArray.getColor(R.styleable.IndicatorTextView_normalColor, Color.GRAY);
        mSelectedTextColor = typedArray.getColor(R.styleable.IndicatorTextView_selectedColor, Color.RED);
        mDrawNormalTextColor = mNormalTextColor;
        mDrawSelectedTextColor = mSelectedTextColor;
        mRect = new RectF();
        mTextRect = new Rect();
    }

    @Override
    public void draw(Canvas canvas) {
        String text = getText().toString();
        mPaint.setTypeface(getTypeface());
        mPaint.setColor(mDrawNormalTextColor);
        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(text, 0, text.length(), mTextRect);
        float x = -mTextRect.left + (getWidth() - mPaint.measureText(text)) / 2;
        float y = (getHeight() - mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) / 2;
        canvas.drawText(text, x, y, mPaint);

        canvas.saveLayer(mRect, mPaint, Canvas.ALL_SAVE_FLAG);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(mRect, mPaint);
        mPaint.setXfermode(null);
        canvas.restore();

        canvas.saveLayer(mRect, mPaint, Canvas.ALL_SAVE_FLAG);
        mPaint.setColor(mDrawSelectedTextColor);
        canvas.drawText(text, x, y, mPaint);
        canvas.restore();
    }

    public void updateRect(float offset, boolean isNext) {
        mDrawNormalTextColor = isNext ? mNormalTextColor : mSelectedTextColor;
        mDrawSelectedTextColor = isNext ? mSelectedTextColor : mNormalTextColor;
        mRect.set(getTranslationX(), getTranslationY(), getTranslationX() + getWidth() * offset, getTranslationY() + getHeight());
        invalidate();
    }
}
