package nhatan172.noteapp.NoteModel;

/**
 * Created by nhata on 21/04/2017.
 */

public class Note {
    private int index;
    private  String note;
    private String title;
    private String updated_time;
    private String color;
    private boolean hasAlarm = false;
    private String time_alarm = "";

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public void setTime_alarm(String time_alarm) {
        this.time_alarm = time_alarm;
    }

    public String getTime_alarm() {

        return time_alarm;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public String getNote() {
        return note;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public String getColor() {
        return color;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
