package com.example.kieuptn.demoapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KieuPTN on 7/9/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private final Context mContext;
    private final TabHost mTabHost;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    static final class TabInfo {
        private final String tag;
        private final Fragment mFragment;
        private final Bundle args;

        TabInfo(String _tag, Fragment _fragment, Bundle _args) {
            tag = _tag;
            mFragment = _fragment;
            args = _args;
        }
    }
    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Fragment fragment, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, fragment, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);

        return info.mFragment;

    }

    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        // Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);
    }

    public void onPageScrollStateChanged(int state) {
    }
}
