package nhatan172.noteapp.model;

/**
 * Created by nhata on 21/04/2017.
 */

public class Note {
    private int mIndex;
    private String mNote;
    private String mTitle;
    private String mUpdatedTime;
    private String mColor;
    private boolean hasAlarm = false;
    private String mTimeAlarm = "";

    public void setHasAlarm(boolean has) {
        hasAlarm = has;
    }

    public void setTimeAlarm(String timeAlarm) {
        mTimeAlarm = timeAlarm;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setUpdatedTime(String updated_time) {
        mUpdatedTime = updated_time;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getTimeAlarm() {
        return mTimeAlarm;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public String getNote() {
        return mNote;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUpdatedTime() {
        return mUpdatedTime;
    }

    public String getColor() {
        return mColor;
    }

    public int getIndex() {
        return mIndex;
    }


}
