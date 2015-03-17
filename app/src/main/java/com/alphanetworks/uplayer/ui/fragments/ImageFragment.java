package com.alphanetworks.uplayer.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphanetworks.uplayer.R;

/**
 * Created by 20001962 on 15-3-19.
 */
public class ImageFragment extends Fragment {

	private static final String TAG = ImageFragment.class.getSimpleName();

	private Context mContext = null;

	public static ImageFragment newInstance() {
		Bundle bundle = new Bundle();

		ImageFragment fragment = new ImageFragment();

		fragment.setArguments(bundle);
		return fragment;

	}

	public ImageFragment() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e(TAG, "Fragment attached");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "Fragment created");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.e(TAG, "Fragment creating view");
        mContext = inflater.getContext();
        View contentView = inflater.inflate(R.layout.fragment_image,container,false);

        return contentView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		Log.e(TAG, "Fragment view created.");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(TAG, "Fragment started");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "Fragment resumed");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(TAG, "Fragment paused");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(TAG, "Fragment stopped");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "Fragment destroyed");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(TAG, "Fragment detached");
	}

}
