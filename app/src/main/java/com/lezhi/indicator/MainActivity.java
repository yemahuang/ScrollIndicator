package com.lezhi.indicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ScrollIndicator.ItemListener {

    private ScrollIndicator mScrollIndicator;
    private ViewPager mViewPager;
    private ArrayList<View> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        mScrollIndicator = (ScrollIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.view_page);

        initTestData();
        mScrollIndicator.setOnItemClick(this);
        mScrollIndicator.setIndicator(R.layout.indicator_layout);
        mScrollIndicator.setViewPager(mViewPager);
        mScrollIndicator.setAdapter(new IndicatorAdapter());
    }

    @Override
    public void onItemClick(View v, int position) {
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void onItemSelected(View v, int position, List<ScrollIndicator.Item> mItems) {
        for (ScrollIndicator.Item item : mItems) {
            TextView textView = (TextView) item.mItemView.findViewById(R.id.tag_text);
            textView.setTextColor(Color.GRAY);
            textView.setBackgroundColor(Color.WHITE);
        }
        TextView textView = (TextView) v.findViewById(R.id.tag_text);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void initTestData() {
        mList = new ArrayList<View>();
        List<String> itemTexts = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10 ; i++) {
            TextView view = new TextView(MainActivity.this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            view.setText(i + "");
            view.setGravity(Gravity.CENTER);
            view.setTextSize(50);
            view.setTextColor(Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            mList.add(view);
            itemTexts.add("" + random.nextInt(1000000) / random.nextInt(1000) + 1);
        }

        for (int position = 0; position < itemTexts.size(); position++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.indicator_item_layout, null);
            TextView textView = (TextView) itemView.findViewById(R.id.tag_text);
            textView.setText(itemTexts.get(position));
            ScrollIndicator.Item item = new ScrollIndicator.Item();
            item.mItemView = itemView;
            item.mPosition = position;
            mScrollIndicator.addItem(item);
        }
    }

    class IndicatorAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
