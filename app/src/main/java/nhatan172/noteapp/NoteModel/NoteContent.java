package nhatan172.noteapp.NoteModel;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by nhata on 29/04/2017.
 */

public class NoteContent {
    private NoteDbHelper mNoteDbHelper;
    public static ArrayList<Note> noteContent;

    public NoteContent(NoteDbHelper noteDbHelper){
            this.mNoteDbHelper = noteDbHelper;
    }

    public ArrayList<Note> getNoteContent(){
        Cursor result = mNoteDbHelper.getAllData();
        result.moveToFirst();
        ArrayList<Note> noteContent = new ArrayList<Note>();
        while(result.isAfterLast()==false){
            Note nt = new Note();
            nt.setIndex(result.getInt(4));
            nt.setTitle(result.getString(0));
            nt.setNote(result.getString(1));
            nt.setUpdated_time(result.getString(2));
            nt.setColor(result.getString(3));
            String alarmTime = result.getString(5);
            if(alarmTime!= null && !alarmTime.equals("")){
                nt.setHasAlarm(true);
                nt.setTime_alarm(alarmTime);
            }
            noteContent.add(nt);
            result.moveToNext();
        }
        mNoteDbHelper.close();
        this.noteContent = noteContent;
        return noteContent;
    }

}
