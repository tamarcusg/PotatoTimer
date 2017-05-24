package com.bignerdranch.android.potatotimer;

import android.widget.Chronometer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by garciata on 6/20/2016.
 */
public class PotatoTimer {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_COMPLETED = "completed";
    private static final String JSON_DATE = "date";
    private static final String JSON_WORK_TIME = "work_time";
    private static final String JSON_BREAK_TIME = "break_time";
    private static final String JSON_DESC = "description";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mCompleted;
    private long mWorkTime;
    private long mBreakTime;

    public PotatoTimer() {
        mId = UUID.randomUUID();
        mDate = new Date();

    }

    public PotatoTimer(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)) {
            mTitle = json.getString(JSON_TITLE);
        }
        mCompleted = json.getBoolean(JSON_COMPLETED);
        mDate = new Date(json.getLong(JSON_DATE));
        mWorkTime = json.getLong(JSON_WORK_TIME);
        mBreakTime = json.getLong(JSON_BREAK_TIME);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_COMPLETED, mCompleted);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_WORK_TIME, mWorkTime);
        json.put(JSON_BREAK_TIME, mBreakTime);
        return json;
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismCompleted() {
        return mCompleted;
    }

    public void setmCompleted(boolean mCompleted) {
        this.mCompleted = mCompleted;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public long getmWorkTime() {
        return mWorkTime;
    }

    public void setmWorkTime(long mWorkTime) {
        this.mWorkTime = mWorkTime;
    }

    public long getmBreakTime() {
        return mBreakTime;
    }

    public void setmBreakTime(long mBreakTime) {
        this.mBreakTime = mBreakTime;
    }
}
