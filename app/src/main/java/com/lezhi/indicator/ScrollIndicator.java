package com.lezhi.indicator;

import android.content.Context;
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

    private FrameLayout mContentLayout;
    private LinearLayout mLinearLayout;

    private View mIndicator;
    private List<Item> mItems;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private float mOffset;
    private boolean mFirstLayout = true;

    public ScrollIndicator(Context context) {
        this(context, null);
    }

    public ScrollIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mItems = new ArrayList<Item>();
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
            }
        });
    }

    public void scrollIndicator(final int position, float offset) {
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
            scrollTo((int)(previousLeft - (getMeasuredWidth() - mOffset) + (previousWidth - (int) (deviation * offset)) / 2 - previousWidth * offset), getScrollY());
        } else {
            int newWidth = previousWidth + (int) (deviation * offset);
            mIndicator.setLeft((int) (previousLeft + previousWidth * offset));
            mIndicator.setRight((int) (previousLeft + newWidth + previousWidth * offset));
            scrollTo((int)(previousLeft - mOffset + (previousWidth + (int) (deviation * offset)) / 2 + previousWidth * offset), getScrollY());
        }
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        if (!smoothScroll && !mFirstLayout)
            scrollToItem(position);
        if (mViewPager != null)
            mViewPager.setCurrentItem(position, smoothScroll);
    }

    public void scrollToItem(int position) {
        View childPrevious = mItems.get(position).mItemView;
        int previousLeft = childPrevious.getLeft();
        int previousWidth = childPrevious.getWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            smoothScrollTo((int)(previousLeft - (getMeasuredWidth() - mOffset) + previousWidth / 2), getScrollY());
        } else {
            smoothScrollTo((int)(previousLeft - mOffset + previousWidth / 2), getScrollY());
        }
    }

    public void updateSelectedItem(int position) {
        if (mItemListener != null)
            mItemListener.onItemSelected(mItems.get(position).mItemView, position, mItems);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mFirstLayout = false;
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
        if (mViewPager != null)
            mViewPager.setAdapter(adapter);
    }

    public void setOffset(float offset) {
        mOffset = offset;
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
