package com.alphanetworks.uplayer.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.alphanetworks.uplayer.R;
import com.alphanetworks.uplayer.ui.fragments.ImageFragment;
import com.alphanetworks.uplayer.ui.fragments.MusicFragment;
import com.alphanetworks.uplayer.ui.fragments.VideoFragment;

/**
 * Created by 20001962 on 15-3-19.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = ViewPagerAdapter.class.getSimpleName();
	private static final int PAGER_NUM = 3;

	private static String[] PAGER_TITLES = null;

	private Context mContext = null;

	public ViewPagerAdapter(Context context, FragmentManager manager) {
		super(manager);
		mContext = context;
		Resources res = mContext.getResources();

		PAGER_TITLES = new String[] { res.getString(R.string.indicator_music),
				res.getString(R.string.indicator_video),
				res.getString(R.string.indicator_image) };

	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;

		switch (position) {
		case 0:
            Log.e(TAG,"0000000000000");
            fragment = MusicFragment.newInstance();
			break;
		case 1:
            Log.e(TAG,"1111111111111");
			fragment = VideoFragment.newInstance();
			break;
		case 2:
            Log.e(TAG,"2222222222222");
			fragment = ImageFragment.newInstance();
			break;
		default:
			break;
		}

		return fragment;
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return PAGER_TITLES[position];
	}

	@Override
	public int getCount() {
		return PAGER_NUM;
	}
}
