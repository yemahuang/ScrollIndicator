package com.lezhi.indicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ScrollIndicator.ItemListener {

    private ScrollIndicator mScrollIndicator;
    private ViewPager mViewPager;
    private ArrayList<View> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
    }

    private void initView() {
        mScrollIndicator = (ScrollIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.view_page);

        mList = new ArrayList<View>();
        List<String> itemTexts = new ArrayList<>();
        mViewPager.addOnPageChangeListener(this);
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
            itemTexts.add(" " + random.nextInt(10000000) / random.nextInt(100) + " ");
        }
        mScrollIndicator.setData(itemTexts);
        mScrollIndicator.setOnItemClick(this);
        mScrollIndicator.setItemTextColor(Color.BLACK);
        mScrollIndicator.setItemSelectedTextColor(Color.RED);
        mScrollIndicator.setIndicator(R.layout.indicator_layout);
        mViewPager.setAdapter(new PagerAdapter() {
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
        });
        mViewPager.setCurrentItem(7);
        mScrollIndicator.scrollToItem(7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mScrollIndicator.scrollIndicator(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(View v, int position) {
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void onItemSelected(List<ScrollIndicator.Item> items, View v, int position) {
        for (ScrollIndicator.Item item : items) {
            ((TextView)item.mItemView).setTextColor(mScrollIndicator.getItemTextColor());
            item.mItemView.setBackgroundColor(mScrollIndicator.getItemBgColor());
        }
        ((TextView)v).setTextColor(mScrollIndicator.getItemSelectedTextColor());
        v.setBackgroundColor(mScrollIndicator.getItemSelectedBgColor());
    }
}
