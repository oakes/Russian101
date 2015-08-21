package net.sekao.russian101;

import android.widget.BaseAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.SparseArray;

public class LessonDetailAdapter extends BaseAdapter {
	private Context mContext;
	private int mLessonNum;
	private int mPageCount;
	private int mSelected = -1;
	private SharedPreferences mSettings;
	private SparseArray<Bitmap> mPageBitmaps = new SparseArray<Bitmap>();

	public LessonDetailAdapter(Context c, int lessonNum, int pageCount) {
		mContext = c;
		mLessonNum = lessonNum;
		mPageCount = pageCount;

		for (int i = 0; i < mPageCount; i++) {
			mPageBitmaps.put(i, TouchImage.getLessonPage(c, mLessonNum, i, true));
		}

		mSettings = PreferenceManager.getDefaultSharedPreferences(c);
		refresh();
	}

	public void refresh() {
		mSelected = getSelection();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPageCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setSelection(int position) {
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putInt("lesson", mLessonNum);
		editor.putInt("page", position);
		editor.commit();

		mSelected = position;
		notifyDataSetChanged();
	}

	public int getSelection() {
		if (mSettings.getInt("lesson", 0) == mLessonNum) {
			return mSettings.getInt("page", 0);
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TouchImage imageView;

		if (convertView == null) {
			imageView = new TouchImage(mContext);
		} else {
			imageView = (TouchImage) convertView;
		}

		imageView.setBackgroundResource(position == mSelected ? R.color.listitem : Color.TRANSPARENT);
		imageView.setLessonPage(mPageBitmaps.get(position));

		return imageView;
	}
}