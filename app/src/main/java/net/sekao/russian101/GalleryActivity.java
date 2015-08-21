package net.sekao.russian101;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class GalleryActivity extends Activity {
	private GalleryAdapter mGallery;
	private ViewPager mPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_pages);
		
		int lesson = 0, page = 0, count = 0, seek = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			lesson = extras.getInt("lesson");
			page = extras.getInt("page");
			count = extras.getInt("count");
		}
		
		if (savedInstanceState != null) {
			seek = savedInstanceState.getInt("seek", 0);
		}
		
		mGallery = new GalleryAdapter(this, lesson, count, seek);
		mPager = (ViewPager) findViewById(R.id.view_pager);
		mPager.setAdapter(mGallery);
		mPager.setCurrentItem(page);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mGallery.stopMediaController();
		mGallery.stopMediaPlayer();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mGallery.startMediaPlayer();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mGallery.pauseMediaPlayer();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("seek", mGallery.getCurrentPosition());
	}
	
	public void setSelection(int position) {
		mPager.setCurrentItem(position);
	}
}