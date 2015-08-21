package net.sekao.russian101;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

public class LessonListActivity extends Activity
		implements LessonListFragment.Callbacks {

	private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_list);

		if (findViewById(R.id.lesson_detail_container) != null) {
			mTwoPane = true;

			LessonListFragment fragment =
					(LessonListFragment) getFragmentManager()
					.findFragmentById(R.id.lesson_list);
			fragment.setActivateOnItemClick(true);

			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			int lesson = settings.getInt("lesson", 0);
			if (lesson >= 0) {
				fragment.setSelection(lesson);
			}
		}
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(LessonDetailFragment.ARG_ITEM_ID, id);
			LessonDetailFragment fragment = new LessonDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.lesson_detail_container, fragment)
					.commit();
		} else {
			Intent detailIntent = new Intent(this, LessonDetailActivity.class);
			detailIntent.putExtra(LessonDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
