package com.lezhi.indicator;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lezhi on 2017/7/25.
 */

public class ScrollIndicator extends HorizontalScrollView {

    private Rect mIndicatorRect;
    private Rect mRect;
    private FrameLayout mContentLayout;
    private LinearLayout mLinearLayout;

    private View mIndicator;
    private int mPosition;
    private List<Item> mItems;

    private int mItemBgColor;
    private int mItemTextColor;

    private int mItemSelectedBgColor;
    private int mItemSelectedTextColor;


    public ScrollIndicator(Context context) {
        this(context, null);
    }

    public ScrollIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mItems = new ArrayList<Item>();
        mRect = new Rect();
        mIndicatorRect = new Rect();
        mContentLayout = new FrameLayout(context);
        addView(mContentLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentLayout.addView(mLinearLayout);

    }

    public void setIndicator(View indicator) {
        mIndicator = indicator;
        mContentLayout.addView(mIndicator);
    }

    public void setIndicator(int layoutRes) {
        mIndicator = LayoutInflater.from(getContext()).inflate(layoutRes, mContentLayout, false);
        mContentLayout.addView(mIndicator);
    }

    public void addItem(final Item item) {
        mItems.add(item);
        mLinearLayout.addView(item.mItemView);
        item.mItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onItemClick(v, item.mPosition);
                scrollToItem(item.mPosition);
            }
        });
    }

    public void setData(List<String> itemTexts) {
        for (int position = 0; position < itemTexts.size(); position++) {
            TextView textView = new TextView(getContext());
            textView.setText(itemTexts.get(position));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mItemTextColor);
            textView.setTextSize(30);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(mItemBgColor);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ScrollIndicator.Item item = new ScrollIndicator.Item();
            item.mItemView = textView;
            item.mPosition = position;
            addItem(item);
        }
    }

    public void scrollIndicator(final int position, float offset) {
        mLinearLayout.getLocalVisibleRect(mRect);
        mIndicator.getLocalVisibleRect(mIndicatorRect);
        View childPrevious = mItems.get(position).mItemView;

        int previousLeft = childPrevious.getLeft();
        int previousWidth = childPrevious.getWidth();
        int nextWidth = 0;
        if (position < mItems.size() - 1) {
            View childNext = mItems.get(position + 1).mItemView;
            nextWidth = childNext.getWidth();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            int deviation = nextWidth == 0 ? 0 : nextWidth - previousWidth;
            int newWidth = (int) (deviation * offset);

            mIndicator.setLeft((int) (previousLeft - newWidth - (previousWidth * offset)));
            mIndicator.setRight((int) (previousLeft + previousWidth - (previousWidth * offset)));
        } else {
            int deviation = nextWidth == 0 ? 0 : nextWidth - previousWidth;
            int newWidth = previousWidth + (int) (deviation * offset);

            mIndicator.setLeft((int) (previousLeft + previousWidth * offset));
            mIndicator.setRight((int) (previousLeft + newWidth + previousWidth * offset));
        }
        if (position > mPosition || position < mPosition || offset == 0) {
            scrollToItem(position);
        }
    }

    public void scrollToItem(int position) {
        mPosition = position;
        mLinearLayout.getLocalVisibleRect(mRect);
        mIndicator.getLocalVisibleRect(mIndicatorRect);
        if (mIndicatorRect.isEmpty())
            updateIndicatorLayout(position);
        int left = mIndicator.getLeft();
        int right = mIndicator.getRight();
        int width = mIndicator.getWidth();
        int scrollX = 0;
        if (left < mRect.left) {
            scrollX = -width;
            if (mIndicatorRect.isEmpty()) {
                scrollX = -width - (mRect.left - left);
                smoothScrollBy(scrollX, 0);
            }
        }
        if (right > mRect.right) {
            scrollX = width;
            if (mIndicatorRect.isEmpty()) {
                scrollX = width + right - mRect.right;
                smoothScrollBy(scrollX, 0);
                return;
            }
        }
        smoothScrollBy(scrollX, 0);
        if (mItemListener != null) {
            mItemListener.onItemSelected(mItems, mItems.get(position).mItemView, position);
        }
    }

    private void updateIndicatorLayout(int position) {
        View tempView = mItems.get(position).mItemView;
        mIndicator.getLayoutParams().width = tempView.getMeasuredWidth();
        mIndicator.setLeft(tempView.getLeft());
        mIndicator.setRight(tempView.getRight());
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mIndicatorRect.isEmpty()) scrollToItem(mPosition);
    }

    public interface ItemListener {
        void onItemClick(View v, int position);

        void onItemSelected(List<Item> items, View v, int position);
    }

    private ItemListener mItemListener;

    public void setOnItemClick(ItemListener itemListener) {
        mItemListener = itemListener;
    }

    public void setItemBgColor(int itemBgColor) {
        mItemBgColor = itemBgColor;
    }

    public int getItemBgColor() {
        return mItemBgColor;
    }

    public void setItemTextColor(int itemTextColor) {
        mItemTextColor = itemTextColor;
    }

    public int getItemTextColor() {
        return mItemTextColor;
    }

    public int getItemSelectedBgColor() {
        return mItemSelectedBgColor;
    }

    public void setItemSelectedBgColor(int itemSelectedBgColor) {
        mItemSelectedBgColor = itemSelectedBgColor;
    }

    public int getItemSelectedTextColor() {
        return mItemSelectedTextColor;
    }

    public void setItemSelectedTextColor(int itemSelectedTextColor) {
        mItemSelectedTextColor = itemSelectedTextColor;
    }

    public static class Item {
        public View mItemView;
        public int mPosition;
    }
}
