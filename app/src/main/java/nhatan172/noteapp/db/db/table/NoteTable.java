package nhatan172.noteapp.db.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.model.Note;

public class NoteTable {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_NOTE = "note";
    public static final String COLUMN_NAME_DATE = "update_time";
    public static final String COLUMN_NAME_COLOR = "color";
    public static final String COLUMN_NAME_TIMEALARM = "time_alarm";
    private DatabaseManager mDatabaseManager;

    public NoteTable(DatabaseManager databaseManager){
        mDatabaseManager = databaseManager;
    }
    public long insertNote(Note note){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TITLE, note.getTitle());
        contentValues.put(COLUMN_NAME_NOTE, note.getNote());
        contentValues.put(COLUMN_NAME_COLOR, note.getColor());
        contentValues.put(COLUMN_NAME_DATE, note.getUpdatedTime());
        contentValues.put(COLUMN_NAME_TIMEALARM,note.getTimeAlarm());
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        String query = "SELECT "+ COLUMN_NAME_TITLE + ", " + COLUMN_NAME_NOTE + ", " + COLUMN_NAME_DATE + ", "
                + COLUMN_NAME_COLOR + ", " +"_id, " + COLUMN_NAME_TIMEALARM + " from notes order by _id asc";
        Cursor res = db.rawQuery(query,null);
        return res ;
    }
    public void updateNote(Note note ){
        String alarmTime = "";
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TITLE,note.getTitle());
        contentValues.put(COLUMN_NAME_NOTE, note.getNote());
        contentValues.put(COLUMN_NAME_COLOR, note.getColor());
        contentValues.put(COLUMN_NAME_DATE,note.getUpdatedTime());
        if(note.hasAlarm())
            alarmTime = note.getTimeAlarm();
        contentValues.put(COLUMN_NAME_TIMEALARM,alarmTime);
        db.update(TABLE_NAME, contentValues, "_id=" + note.getIndex(), null);
    }
    public int deleteNote(int iD){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id="+iD,null);
    }
    public int updateAlarmColumn(int iD){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_DATE,"");
        return db.update(TABLE_NAME, contentValues, "_id=" + iD, null);
    }
}
