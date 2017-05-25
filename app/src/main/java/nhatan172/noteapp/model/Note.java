package nhatan172.noteapp.model;

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

    public boolean equals(Note note){
        if (!mTitle.equals(note.getTitle()))
            return false;
        if (!mNote.equals(note.getTitle()))
            return false;
        if (hasAlarm != note.hasAlarm)
            return false;
        if (hasAlarm)
            return mTimeAlarm.equals(note.getTimeAlarm());
        else return true;
    }
}
