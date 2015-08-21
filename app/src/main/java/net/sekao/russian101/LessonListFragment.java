package net.sekao.russian101;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LessonListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private static List<HashMap<String, String>> MENU_ITEMS;

	private Callbacks mCallbacks = sMenuCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;

	public interface Callbacks {
		public void onItemSelected(String id);
	}

	private static Callbacks sMenuCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] from = new String[] {"title", "subtitle"};
		int[] to = new int[] {android.R.id.title, R.id.subtitle};

		MENU_ITEMS = new ArrayList<HashMap<String, String>>();
		MENU_ITEMS.add(createMenuItem("Alphabet", "алфавит"));
		MENU_ITEMS.add(createMenuItem("Meeting People", "Знакомство"));
		MENU_ITEMS.add(createMenuItem("Family", "семья"));
		MENU_ITEMS.add(createMenuItem("Where do you work?", "Где вы работаете?"));
		MENU_ITEMS.add(createMenuItem("Where do you live?", "Где вы живете?"));
		MENU_ITEMS.add(createMenuItem("Shopping", "покупки"));
		MENU_ITEMS.add(createMenuItem("In the restaurant", "В ресторане"));
		MENU_ITEMS.add(createMenuItem("Transportation", "транспорт"));
		MENU_ITEMS.add(createMenuItem("In the hotel", "В гостинице"));
		MENU_ITEMS.add(createMenuItem("The telephone", "телефон"));

		setListAdapter(new LessonListAdapter(
			getActivity(),
			MENU_ITEMS,
			R.layout.activity_lesson_row,
			from,
			to
		));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setSelector(R.drawable.selector);
		if (savedInstanceState != null && savedInstanceState
				.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sMenuCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		setSelection(position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	@Override
	public void setSelection(int position) {
		((LessonListAdapter) getListAdapter()).setSelection(position);
		mCallbacks.onItemSelected(Integer.toString(position));
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick
				? ListView.CHOICE_MODE_SINGLE
				: ListView.CHOICE_MODE_NONE);
	}

	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	private static HashMap<String, String> createMenuItem(String title, String subtitle) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		map.put("subtitle", subtitle);
		return map;
	}
}
