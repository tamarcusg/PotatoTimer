package com.bignerdranch.android.potatotimer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by garciata on 6/20/2016.
 */
public class PotatoTimerFragment extends Fragment {

    public static final String EXTRA_TIMER_ID = "com.bignerdranch.android.criminalintent.potato_timer_id";
    public static final long WORK_TIME = 1500000;
    public static final long BREAK_TIME = 300000;
    public static final long LONG_BREAK_TIME = 600000;
    public long mTotalWorkTime = 0;
    public long mTotalBreakTime = 0;
    private PotatoTimer mPotatoTimer;
    private EditText mTitleField;
    private Button mWorkButton;
    private Button mBreakButton;
    private Button mPauseButton;
    private Button mDateButton;
    private CheckBox mCompleteCheckBox;
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private TextView mTimer;
    private int breakCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID potatoTimerId = (UUID)getArguments().getSerializable(EXTRA_TIMER_ID);
        setHasOptionsMenu(true);
        mPotatoTimer = SingletonPotatoTimer.get(getActivity()).getmPotatoTimer(potatoTimerId);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_potato_timer, parent, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        mTitleField = (EditText)v.findViewById(R.id.title_potato_timer);
        mTitleField.setText(mPotatoTimer.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPotatoTimer.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTimer = (TextView)v.findViewById(R.id.timer_textView);

        mDateButton = (Button)v.findViewById(R.id.date_potato_timer);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mPotatoTimer.getmDate());
                dialog.setTargetFragment(PotatoTimerFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mCompleteCheckBox = (CheckBox)v.findViewById(R.id.completed_checkBox);
        mCompleteCheckBox.setChecked(mPotatoTimer.ismCompleted());
        mCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPotatoTimer.setmCompleted(isChecked);
            }
        });





        mWorkButton = (Button)v.findViewById(R.id.button_work_potato_timer);
        mWorkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTimer.setText("");
                mWorkButton.setEnabled(false);
                mBreakButton.setEnabled(true);
                mPauseButton.setEnabled(true);
                new CountDownTimer(WORK_TIME, 1000) {
                    public void onTick(long millisUntilFinished) {
                        if (!mBreakButton.isEnabled() || mWorkButton.isEnabled()) {
                            cancel();
                        } else {
                            mTotalWorkTime += 1;
                            mPotatoTimer.setmWorkTime(mTotalWorkTime);
                            mTimer.setText(""+String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                    }

                    public void onFinish() {
                        mTimer.setText("Take a break");
                    }
                }.start();

            }
        });

        mBreakButton = (Button)v.findViewById(R.id.button_break_potato_timer);
        mPotatoTimer.setmBreakTime(mPotatoTimer.getmBreakTime());
        mBreakButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mWorkButton.setEnabled(true);
                mBreakButton.setEnabled(false);
                mPauseButton.setEnabled(true);

                if (breakCount != 3) {
                    new CountDownTimer(BREAK_TIME, 1000) {
                        public void onTick(long millisUntilFinished) {
                            if (!mWorkButton.isEnabled() || mBreakButton.isEnabled()) {
                                cancel();
                            } else {
                                mTotalBreakTime += 1;
                                mPotatoTimer.setmBreakTime(mTotalBreakTime);
                                mTimer.setText("" + String.format("%d min, %d sec",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }
                        }

                        public void onFinish() {
                            mTimer.setText("Get back to work");
                            breakCount += 1;
                        }
                    }.start();
                } else {
                    breakCount = 0;
                    new CountDownTimer(LONG_BREAK_TIME, 1000) {
                        public void onTick(long millisUntilFinished) {
                            if (!mWorkButton.isEnabled() || mBreakButton.isEnabled()) {
                                cancel();
                            } else {
                                mTotalBreakTime += 1;
                                mPotatoTimer.setmBreakTime(mTotalBreakTime);
                                mTimer.setText(""+String.format("%d min, %d sec",
                                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }
                        }

                        public void onFinish() {
                            mTimer.setText("Get back to work");
                        }
                    }.start();
                }



            }
        });

        mPauseButton = (Button)v.findViewById(R.id.button_stop_progress);
        mPauseButton.setEnabled(false);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mWorkButton.setEnabled(true);
                mBreakButton.setEnabled(true);
                mPauseButton.setEnabled(false);
            }
        });

        return v;
    }


    private void updateDate() {
        mDateButton.setText(mPotatoTimer.getmDate().toString());
    }

    public static PotatoTimerFragment newInstance(UUID potatoTimerId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIMER_ID, potatoTimerId);

        PotatoTimerFragment fragment = new PotatoTimerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mPotatoTimer.setmDate(date);
            updateDate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mPauseButton.isEnabled() == false) {
                    if (NavUtils.getParentActivityName(getActivity()) != null) {
                        NavUtils.navigateUpFromSameTask(getActivity());
                    }

                } else {
                    Toast.makeText(getActivity(),
                            R.string.toast,
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SingletonPotatoTimer.get(getActivity()).savePotatoTimers();
    }
}
