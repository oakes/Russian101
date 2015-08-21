package net.sekao.russian101;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class LessonDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_detail);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(LessonDetailFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(LessonDetailFragment.ARG_ITEM_ID));
			LessonDetailFragment fragment = new LessonDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.lesson_detail_container, fragment)
					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this, LessonListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
