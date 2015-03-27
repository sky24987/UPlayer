package com.alphanetworks.uplayer.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphanetworks.uplayer.R;

/**
 * Created by 20001962 on 2015/3/26.
 */
public class MusicAdapter extends
		RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

	private static final String TAG = MusicAdapter.class.getSimpleName();

	private int mMarkPosition = -1;

	public static class MusicViewHolder extends RecyclerView.ViewHolder {

		private TextView mTitleView = null;
		private TextView mSignerView = null;
		private TextView mAlbumView = null;
		private ImageView mAlbumThumbView = null;

		public MusicViewHolder(View itemView) {
			super(itemView);
			mTitleView = (TextView) itemView.findViewById(R.id.music_title);
			mSignerView = (TextView) itemView.findViewById(R.id.music_signer);
			mAlbumView = (TextView) itemView.findViewById(R.id.music_album);
			mAlbumThumbView = (ImageView) itemView
					.findViewById(R.id.music_album_thumb);
		}

		public TextView getTitleView() {
			return mTitleView;
		}

		public TextView getSignerView() {
			return mSignerView;
		}

		public TextView getAlbumView() {
			return mAlbumView;
		}

		public ImageView getAlbumThumbView() {
			return mAlbumThumbView;
		}
	}

	@Override
	public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View contentView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_music, parent, false);

		return new MusicViewHolder(contentView);
	}

	@Override
	public void onBindViewHolder(MusicViewHolder holder, int position) {

		holder.getTitleView().setText("Item " + position);
		holder.getSignerView().setText("Katy Perry");
		holder.getAlbumView().setText("It's all of me");
		holder.getAlbumThumbView().setImageResource(R.mipmap.album);

		View item = holder.itemView;
		if (position > mMarkPosition) {
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(item,
					"translationY", item.getMeasuredHeight(), 0);
			ObjectAnimator animator2 = ObjectAnimator.ofFloat(item, "alpha",
					0.2f, 1.0f);
			AnimatorSet set = new AnimatorSet();
			set.setDuration(300);
			set.playTogether(animator1, animator2);
			set.start();
		} else {
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(item,
					"translationY", -item.getMeasuredHeight(), 0);
			ObjectAnimator animator2 = ObjectAnimator.ofFloat(item, "alpha",
					0.2f, 1.0f);
			AnimatorSet set = new AnimatorSet();
			set.setDuration(300);
			set.playTogether(animator1, animator2);
			set.start();
		}
		mMarkPosition = position;
	}

	@Override
	public int getItemCount() {
		return 100;
	}
}
