# ScrollIndicator
android ScrollIndicator use with ViewPager
滚动指示器，自适应不同tab的宽度

    
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
     