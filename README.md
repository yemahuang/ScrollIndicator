# ScrollIndicator
android ScrollIndicator use with ViewPager
滚动指示器，自适应不同tab的宽度

 // 初始化

 mScrollIndicator.setData(itemTexts);
 mScrollIndicator.setOnItemClick(this);
 mScrollIndicator.setItemTextColor(Color.BLACK);
 mScrollIndicator.setItemSelectedTextColor(Color.RED);
 mScrollIndicator.setIndicator(R.layout.indicator_layout
 
 // 实现监听接口
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
