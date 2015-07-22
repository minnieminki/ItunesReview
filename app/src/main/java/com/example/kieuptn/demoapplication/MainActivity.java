package com.example.kieuptn.demoapplication;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pages);

        // create fragment
        FragmentTopSong fragmentTopSong = new FragmentTopSong();
        FragmentFavories fragmentFavories = new FragmentFavories();
        FragmentSearch fragmentSearch = new FragmentSearch();
        FragmentPlaylist fragmentPlaylist = new FragmentPlaylist();

        // create and add tab in tabAdapter
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(mTabHost.newTabSpec("topsong").setIndicator("TopSong"), fragmentTopSong, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("favories").setIndicator("Favories"), fragmentFavories, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("search").setIndicator("Search"), fragmentSearch, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("playlist").setIndicator("Playlist"), fragmentPlaylist, null);

        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
            TextView textView = (TextView)mTabHost.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.title);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

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
}
