package nhatan172.noteapp.utils;

import android.database.Cursor;

import java.util.ArrayList;

import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.model.Note;

/**
 * Created by nhata on 29/04/2017.
 */

public class NoteContent {
    private DatabaseManager mDatabaseManager;
    private NoteTable mNoteTable;
    public static ArrayList<Note> sNoteContent;

    public NoteContent(DatabaseManager databaseManager){
        mDatabaseManager = databaseManager;
        mNoteTable = mDatabaseManager.getNoteTable();
    }

    public ArrayList<Note> getNoteContent(){
        Cursor result = mNoteTable.getAllData();
        result.moveToFirst();
        ArrayList<Note> noteContent = new ArrayList<Note>();
        while(result.isAfterLast()==false){
            Note nt = new Note();
            nt.setIndex(result.getInt(4));
            nt.setTitle(result.getString(0));
            nt.setNote(result.getString(1));
            nt.setUpdatedTime(result.getString(2));
            nt.setColor(result.getString(3));
            String alarmTime = result.getString(5);
            if(alarmTime!= null && !alarmTime.equals("")){
                nt.setHasAlarm(true);
                nt.setTimeAlarm(alarmTime);
            }
            noteContent.add(nt);
            result.moveToNext();
        }
        sNoteContent = noteContent;
        return noteContent;
    }

}
