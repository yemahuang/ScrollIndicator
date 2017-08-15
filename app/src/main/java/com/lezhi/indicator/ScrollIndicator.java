package com.lezhi.indicator;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

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
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private PagerAdapter mAdapter;

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

    public void scrollIndicator(final int position, float offset) {
        mPosition = position;
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

        int deviation = nextWidth == 0 ? 0 : nextWidth - previousWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            int newWidth = (int) (deviation * offset);
            mIndicator.setLeft((int) (previousLeft - newWidth - (previousWidth * offset)));
            mIndicator.setRight((int) (previousLeft + previousWidth - (previousWidth * offset)));
            scrollTo((int)(previousLeft - getMeasuredWidth() / 2 + (previousWidth - (int) (deviation * offset)) / 2 - previousWidth * offset), getScrollY());
        } else {
            int newWidth = previousWidth + (int) (deviation * offset);
            mIndicator.setLeft((int) (previousLeft + previousWidth * offset));
            mIndicator.setRight((int) (previousLeft + newWidth + previousWidth * offset));
            scrollTo((int)(previousLeft - getMeasuredWidth() / 2 + (previousWidth + (int) (deviation * offset)) / 2 + previousWidth * offset), getScrollY());
        }
    }

    public void scrollToItem(int position) {
        mPosition = position;
        if (mViewPager != null)
            mViewPager.setCurrentItem(position);
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
        updateSelectedItem(position);
    }

    public void updateSelectedItem(int position) {
        if (mItemListener != null)
            mItemListener.onItemSelected(mItems.get(position).mItemView, position, mItems);
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
        void onItemSelected(View v, int position, List<Item> mItems);
    }

    private ItemListener mItemListener;

    public void setOnItemClick(ItemListener itemListener) {
        mItemListener = itemListener;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new PageChangeListener());
    }

    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        if (mViewPager != null)
            mViewPager.setAdapter(adapter);
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        private int curPos;
        private boolean isPageChange;
        private int curState;

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnPageChangeListener != null)
                mOnPageChangeListener.onPageScrollStateChanged(state);
            curState = state;
            if (ViewPager.SCROLL_STATE_IDLE == state && isPageChange) {
                updateSelectedItem(curPos);
                isPageChange = false;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mOnPageChangeListener != null)
                mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            scrollIndicator(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            if (mOnPageChangeListener != null)
                mOnPageChangeListener.onPageSelected(position);
            curPos = position;
            if (ViewPager.SCROLL_STATE_IDLE == curState) {
                updateSelectedItem(curPos);
            } else {
                isPageChange = true;
            }
        }

    }

    public static class Item {
        public View mItemView;
        public int mPosition;
    }
}
