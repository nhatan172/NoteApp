package nhatan172.noteapp.NoteModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nhata on 14/04/2017.
 */

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                    NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY," +
                    NoteContract.NoteEntry.COLUMN_NAME_TITLE + " TEXT," +
                    NoteContract.NoteEntry.COLUMN_NAME_NOTE + " TEXT,"+
                    NoteContract.NoteEntry.COLUMN_NAME_COLOR + " TEXT,"+
                    NoteContract.NoteEntry.COLUMN_NAME_DATE +" TEXT, "+
                    NoteContract.NoteEntry.COLUMN_NAME_TIMEALARM +" TEXT) ";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NoteDB.db";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertNote(String title, String note, String color,String day_update,String timeAlarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_NOTE, note);
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_COLOR, color);
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_DATE,day_update);
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_TIMEALARM,timeAlarm);
        return db.insert(NoteContract.NoteEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select title, note, update_time, color, _id, time_alarm   from notes order by _id asc",null);
        return res ;
    }
    public void updateNote(Note note ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_TITLE,note.getTitle());
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_NOTE, note.getNote());
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_DATE,note.getUpdated_time());
        String alarmTime = "";
        if(note.hasAlarm())
            alarmTime = note.getTime_alarm();
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_TIMEALARM,alarmTime);
        db.update(NoteContract.NoteEntry.TABLE_NAME, contentValues, "_id=" + note.getIndex(), null);
    }
    public int deleteNote(int iD){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NoteContract.NoteEntry.TABLE_NAME, "_id="+iD,null);
    }
    public int turnOffAlarm(int iD){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.NoteEntry.COLUMN_NAME_DATE,"");
        return db.update(NoteContract.NoteEntry.TABLE_NAME, contentValues, "_id=" + iD, null);
    }

}
