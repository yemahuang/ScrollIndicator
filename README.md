# ScrollIndicator
android ScrollIndicator 
滚动指示器，自适应不同tab的宽度

参考： 网易新闻客户端分类滚动栏，斗鱼直播分类滚动栏的效果

    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:layout_editor_absoluteY="8dp"
        tools:layout_editor_absoluteX="8dp"
        tools:ignore="MissingConstraints">
        

        <com.lezhi.indicator.ScrollIndicator
            android:id="@+id/indicator"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/white" />
            

        <android.support.v4.view.ViewPager
            android:id="@+id/view_page"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
            
    </LinearLayout>
    
    
    
     mScrollIndicator.setItemListener(this);
     mScrollIndicator.setIndicator(R.layout.indicator_layout);
     mScrollIndicator.setViewPager(mViewPager);
     mScrollIndicator.setAdapter(new IndicatorAdapter());
     
     
     // 设置偏移量
     mScrollIndicator.setOffset(getResources().getDisplayMetrics().widthPixels / 2);
     
     ![image]
     (https://github.com/yemahuang/ScrollIndicator/blob/master/offset_center.gif)
    
    
     mScrollIndicator.setOffset(200);
    
     ![image]
     https://github.com/yemahuang/ScrollIndicator/blob/master/offset_200.gif
     
     