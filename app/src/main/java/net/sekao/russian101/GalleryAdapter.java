package net.sekao.russian101;

import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.Context;
import android.content.SharedPreferences;

public class GalleryAdapter extends PagerAdapter
	implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaController.MediaPlayerControl {
	private Context mContext;
	private int mLessonNum;
	private int mPageCount;
	private int mSeek;
	private int mCurrentPage;
	private MediaPlayer mPlayer;
	private GalleryController mController;
	private SharedPreferences mSettings;
	
	public GalleryAdapter(Context context, int lesson, int count, int seek) {
		super();

		mContext = context;
		mLessonNum = lesson;
		mPageCount = count;
		mSeek = seek;
		mCurrentPage = -1;
		mPlayer = null;
		mSettings = PreferenceManager.getDefaultSharedPreferences(context);

		mController = new GalleryController(mContext);
		mController.setAnchorView(((Activity) context).findViewById(R.id.media_controller));
		mController.setMediaPlayer(this);
	}

	public void startMediaPlayer() {
		if (mPlayer == null || mPlayer.isPlaying()) {
			return;
		}

		mPlayer.start();
	}

	public void pauseMediaPlayer() {
		if (mPlayer == null || !mPlayer.isPlaying()) {
			return;
		}

		mPlayer.pause();
	}

	public void createMediaPlayer() {
		if (mCurrentPage < 0) {
			return;
		}

		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		String path = "lesson" + (mLessonNum+1) + "/" + (mCurrentPage+1) + ".ogg";
		try {
			AssetFileDescriptor descriptor = mContext.getAssets().openFd(path);
			mPlayer.setDataSource(
				descriptor.getFileDescriptor(),
				descriptor.getStartOffset(),
				descriptor.getLength()
			);
			descriptor.close();
			mPlayer.setOnPreparedListener(this);
			mPlayer.setOnCompletionListener(this);
			mPlayer.prepare();
			mPlayer.start();

			if (mSeek > 0) {
				mPlayer.seekTo(mSeek);
				mSeek = 0;
			}
		} catch (Exception e) {}
	}

	public void stopMediaController() {
		if (mController != null) {
			mController.setShouldHide(true);
			mController.hide();
		}
	}

	public void stopMediaPlayer() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return mPageCount;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View layout = View.inflate(container.getContext(), R.layout.lesson_page, null);

		TouchImageView imageView = (TouchImageView) layout.findViewById(R.id.lesson_image);
		Bitmap image = TouchImage.getLessonPage(container.getContext(), mLessonNum, position, false);
		imageView.setLessonPage(image);

		container.addView(layout);
		return layout;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (mCurrentPage == position) {
			return;
		}
		mCurrentPage = position;

		SharedPreferences.Editor editor = mSettings.edit();
		editor.putInt("lesson", mLessonNum);
		editor.putInt("page", position);
		editor.commit();

		stopMediaPlayer();
		createMediaPlayer();
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		mController.setShouldHide(false);
		mController.show();
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		if (mCurrentPage + 1 == mPageCount) {
			return;
		}

		((GalleryActivity) mContext).setSelection(mCurrentPage + 1);
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		if (mPlayer == null) {
			return 0;
		}
		return mPlayer.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		if (mPlayer == null) {
			return 0;
		}
		return mPlayer.getDuration();
	}

	@Override
	public boolean isPlaying() {
		if (mPlayer == null) {
			return false;
		}
		return mPlayer.isPlaying();
	}

	@Override
	public void pause() {
		mPlayer.pause();
	}

	@Override
	public void seekTo(int pos) {
		mPlayer.seekTo(pos);
	}

	@Override
	public void start() {
		mPlayer.start();
	}
	
	private class GalleryController extends MediaController {
		private Context mContext;
		private boolean mShouldHide;
		
		public GalleryController(Context context) {
			super(context);
			mContext = context;
			mShouldHide = false;
			setEnabled(true);
		}
		
		@Override
		public void hide() {
			if (mShouldHide) {
				super.hide();
			} else {
				super.show();
			}
		}
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				((Activity) mContext).finish();
				return true;
			}
			return false;
		}

		public void setShouldHide(boolean shouldHide) {
			mShouldHide = shouldHide;
		}
	}
}