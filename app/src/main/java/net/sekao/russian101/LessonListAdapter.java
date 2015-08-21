package net.sekao.russian101;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LessonListAdapter extends SimpleAdapter {
	private Context mContext;
	
	private int mSelected;
	private SharedPreferences mSettings;

	private int mLessonCount;
	
	public LessonListAdapter(Context c, List<HashMap<String, String>> map, int res, String from[], int to[]) {
		super(c, map, res, from, to);

		mContext = c;
		mLessonCount = map.size();
		mSettings = PreferenceManager.getDefaultSharedPreferences(c);

		mSelected = getSelection();
		notifyDataSetChanged();
	}

	public void setSelection(int position) {
		if (mSelected != position) {
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putInt("lesson", position);
			editor.putInt("page", 0);
			editor.commit();

			mSelected = getSelection();
			notifyDataSetChanged();
		}
	}

	public int getSelection() {
		return mSettings.getInt("lesson", 0);
	}

	@Override
	public int getCount() {
		return mLessonCount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout v;
		ColorStateList c;

		if (isEnabled(position)) {
			v = (RelativeLayout) super.getView(position, convertView, parent);
		} else {
			v = (RelativeLayout) super.getView(position - 1, convertView, parent);
		}

		v.setBackgroundResource(position == mSelected ? R.color.listitem : Color.TRANSPARENT);
		c = ColorStateList.valueOf(mContext.getResources().getColor(R.color.maintext))
				.withAlpha(isEnabled(position) ? 255 : 128);
		
		for (int i = 0; i < v.getChildCount(); i++) {
			TextView t = (TextView) v.getChildAt(i);
			t.setTextColor(c);
		}
		
		return v;
	}
}