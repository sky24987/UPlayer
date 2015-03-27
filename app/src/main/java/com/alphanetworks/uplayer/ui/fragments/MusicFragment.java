package com.alphanetworks.uplayer.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphanetworks.uplayer.R;
import com.alphanetworks.uplayer.Utils;
import com.alphanetworks.uplayer.ui.adapter.MusicAdapter;

/**
 * Created by 20001962 on 15-3-19.
 */
public class MusicFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	private static final String TAG = MusicFragment.class.getSimpleName();

	private Context mContext = null;
	private SwipeRefreshLayout mSwipeRefreshLayout = null;
	private RecyclerView mRecyclerView = null;

	public static MusicFragment newInstance() {
		Bundle bundle = new Bundle();

		MusicFragment fragment = new MusicFragment();

		fragment.setArguments(bundle);
		return fragment;

	}

	public MusicFragment() {
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
		View contentView = inflater.inflate(R.layout.fragment_music, container,
				false);

		mSwipeRefreshLayout = (SwipeRefreshLayout) contentView
				.findViewById(R.id.music_swiperefreshlayout);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		TypedValue typedValue = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue,
				true);
		mSwipeRefreshLayout.setColorSchemeColors(typedValue.data);
		mRecyclerView = (RecyclerView) contentView
				.findViewById(R.id.music_recylerview);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.setAdapter(new MusicAdapter());
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

	@Override
	public void onRefresh() {

		Utils.getUiThread().postDelayed(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}, 3000);

	}
}
