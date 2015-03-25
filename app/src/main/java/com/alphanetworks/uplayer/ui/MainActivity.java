package com.alphanetworks.uplayer.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.alphanetworks.uplayer.R;
import com.alphanetworks.uplayer.Utils;
import com.alphanetworks.uplayer.ui.adapter.ViewPagerAdapter;
import com.alphanetworks.uplayer.ui.fragments.DepthPageTransformer;
import com.alphanetworks.uplayer.upnp.dmr.MediaRenderer;
import com.alphanetworks.uplayer.upnp.dms.MediaServer;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;

public class MainActivity extends ActionBarActivity implements
		ServiceConnection {

	private static final String TAG = MainActivity.class.getSimpleName();

	private Context mContext = null;
	private DrawerLayout mNavigation = null;
	private Toolbar mToolbar = null;
	private ViewPager mViewPager = null;
	private ViewPagerAdapter mPagerAdapter = null;

	private MediaServer mMediaServer = null;
	private MediaRenderer mMediaRenderer = null;
	private AndroidUpnpService mUpnpService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "Activity created.");
		setContentView(R.layout.activity_main);

		mContext = this;
		mToolbar = (Toolbar) findViewById(R.id.main_toolbar);

		if (mToolbar != null)
			setSupportActionBar(mToolbar);
		mViewPager = (ViewPager) findViewById(R.id.main_view_pager);

		mPagerAdapter = new ViewPagerAdapter(mContext,
				getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

		// 为viewpager设置切换动画
		mViewPager.setPageTransformer(true, new DepthPageTransformer());

		mNavigation = (DrawerLayout) findViewById(R.id.main_navitagion_layout);
		TypedValue typedValue = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue,
				true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			mNavigation.setStatusBarBackgroundColor(typedValue.data);

		bindService(new Intent(this, AndroidUpnpServiceImpl.class), this,
				Context.BIND_AUTO_CREATE);
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

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder service) {
		Log.e(TAG, "UPNP service is connected.");

		mUpnpService = (AndroidUpnpService) service;

		mMediaServer = new MediaServer(mContext, Utils.getInetAddress(mContext));
		if (mMediaServer != null)
			mUpnpService.getRegistry().addDevice(mMediaServer.getDevice());
		mMediaRenderer = new MediaRenderer(mContext);
		if (mMediaRenderer != null)
			mUpnpService.getRegistry().addDevice(mMediaRenderer.getDevice());
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		Log.e(TAG, "UPNP service is disconnected.");

	}
}
