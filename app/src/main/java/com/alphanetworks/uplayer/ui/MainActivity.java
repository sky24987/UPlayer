package com.alphanetworks.uplayer.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.alphanetworks.uplayer.R;
import com.alphanetworks.uplayer.ui.adapter.ViewPagerAdapter;
import com.alphanetworks.uplayer.ui.widget.SlidingTabLayout;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int[] INDICATOR_COLORS = new int[] {};

	private static final int[] DIVIDER_COLORS = new int[] {};

	private Context mContext = null;

	private DrawerLayout mNavigation = null;
	private Toolbar mToolbar = null;
	private SlidingTabLayout mViewPagerIndicator = null;
	private ViewPager mViewPager = null;
	private ViewPagerAdapter mPagerAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "Activity created.");
		setContentView(R.layout.activity_main);

		mContext = this;
		mToolbar = (Toolbar) findViewById(R.id.main_toolbar);

		if (mToolbar != null)
			setSupportActionBar(mToolbar);

		mViewPagerIndicator = (SlidingTabLayout) findViewById(R.id.main_pager_indicator);

		mViewPager = (ViewPager) findViewById(R.id.main_view_pager);

		mPagerAdapter = new ViewPagerAdapter(mContext,
				getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mViewPagerIndicator.setCustomTabView(R.layout.indicator_layout,R.id.indicator_textview);
        mViewPagerIndicator.setViewPager(mViewPager);


		mNavigation = (DrawerLayout) findViewById(R.id.main_navitagion_layout);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			mNavigation.setStatusBarBackgroundColor(getResources().getColor(
					R.color.pink_400));

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.e(TAG, "Activity started.");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "Activity paused.");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "Activity resumed.");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "Activity stopped.");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "Activity destroyed.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e(TAG, "Activity configuration changed.");
	}
}
