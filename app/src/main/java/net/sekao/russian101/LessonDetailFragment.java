package net.sekao.russian101;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.AdapterView;
import android.content.Intent;

public class LessonDetailFragment extends Fragment {
	public static final String ARG_ITEM_ID = "item_id";
	private Integer mLessonNum;
	private GridView mGridview;

	private int[] mPageCount = {
		35, 9, 8, 13, 8, 27, 23, 18, 18, 24
	};

	public LessonDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mLessonNum = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		getAdapter().refresh();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// create the gridview
		View rootView = inflater.inflate(R.layout.fragment_lesson_detail, container, false);
		mGridview = (GridView) rootView.findViewById(R.id.gridview);

		// create the adapter and set the click listener
		mGridview.setAdapter(new LessonDetailAdapter(
			rootView.getContext(),
			mLessonNum,
			mPageCount[mLessonNum]
		));
		mGridview.setOnItemClickListener(new GridView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((LessonDetailAdapter) parent.getAdapter()).setSelection(position);
				Intent intent = new Intent(parent.getContext(), GalleryActivity.class);
				intent.putExtra("lesson", mLessonNum);
				intent.putExtra("page", position);
				intent.putExtra("count", mPageCount[mLessonNum]);
				parent.getContext().startActivity(intent);
			}
		});

		// set selection if necessary
		setSelection();

		return rootView;
	}

	LessonDetailAdapter getAdapter() {
		return (LessonDetailAdapter) mGridview.getAdapter();
	}

	public void setSelection() {
		if (mGridview == null) {
			return;
		}

		int select = getAdapter().getSelection();
		if (select >= 0) {
			mGridview.setSelection(select);
		}
	}
}
