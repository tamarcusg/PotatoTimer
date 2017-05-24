package com.bignerdranch.android.potatotimer;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by garciata on 6/20/2016.
 */
public class PotatoTimerListFragment extends ListFragment {

    public static final long HOUR = 3600;
    public static final long MINUTE = 60;
    public static final long SECOND = 1;

    private ArrayList<PotatoTimer> mPotatoTimers;
    private static final String TAG = "PotatoTimerFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.title_arraylist_potato_timers);
        mPotatoTimers = SingletonPotatoTimer.get(getActivity()).getmPotatoTimers();

       PotatoTimerAdapter adapter = new PotatoTimerAdapter(mPotatoTimers);
        setListAdapter(adapter);

        setRetainInstance(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PotatoTimer p = ((PotatoTimerAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), PotatoTimerPagerActivity.class);
        i.putExtra(PotatoTimerFragment.EXTRA_TIMER_ID, p.getmId());
        startActivity(i);
    }

    private class PotatoTimerAdapter extends ArrayAdapter<PotatoTimer> {
        public PotatoTimerAdapter(ArrayList<PotatoTimer> potatoTimers) {
            super(getActivity(), 0 , potatoTimers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_potato_timer, null);
            }

            PotatoTimer p = getItem(position);


            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_title_textView);
            titleTextView.setText(p.getmTitle());
            TextView dateTextView = (TextView)convertView.findViewById(R.id.list_item_date_textView);
            dateTextView.setText(p.getmDate().toString());
            TextView workTimeTextView = (TextView)convertView.findViewById(R.id.list_item_work_time_textView);
            workTimeTextView.setText("Wrk: " + toTime(p.getmWorkTime()));
            TextView breakTimeTextView = (TextView)convertView.findViewById(R.id.list_item_break_time_textView);
            breakTimeTextView.setText("Brk: " + toTime(p.getmBreakTime()));
            CheckBox completedCheckBox = (CheckBox)convertView.findViewById(R.id.list_item_completed_checkBox);
            completedCheckBox.setChecked(p.ismCompleted());

            return convertView;

        }
    }

    public String toTime(long millis) {
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        while (millis >= HOUR) {
            millis -= HOUR;
            hours += 1;
        }

        while (millis >= MINUTE) {
            millis -= MINUTE;
            minutes += 1;
        }

        while (millis >= SECOND) {
            millis -= SECOND;
            seconds += 1;
        }

        String time = String.format("%d hr, %d min, %d sec",
                hours, minutes, seconds);

        return time;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((PotatoTimerAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_potato_timer_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_potato_timer:
                PotatoTimer potatoTimer = new PotatoTimer();
                SingletonPotatoTimer.get(getActivity()).addPotatoTimer(potatoTimer);
                Intent i = new Intent(getActivity(), PotatoTimerPagerActivity.class);
                i.putExtra(PotatoTimerFragment.EXTRA_TIMER_ID, potatoTimer.getmId());
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.potato_timer_item_context, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.potato_timer_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_potato_timer:
                        PotatoTimerAdapter adapter = (PotatoTimerAdapter)getListAdapter();
                        SingletonPotatoTimer singletonPotatoTimer = SingletonPotatoTimer.get(getActivity());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (getListView().isItemChecked(i)) {
                                singletonPotatoTimer.deletePotatoTimer(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        return v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        PotatoTimerAdapter adapter = (PotatoTimerAdapter)getListAdapter();
        PotatoTimer potatoTimer = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_potato_timer:
                SingletonPotatoTimer.get(getActivity()).deletePotatoTimer(potatoTimer);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
