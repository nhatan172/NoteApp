package nhatan172.noteapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.model.Note;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NoteDB.db";
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_NOTE = "note";
    public static final String COLUMN_NAME_DATE = "update_time";
    public static final String COLUMN_NAME_COLOR = "color";
    public static final String COLUMN_NAME_TIMEALARM = "time_alarm";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_NOTE + " TEXT,"+
                    COLUMN_NAME_COLOR + " TEXT,"+
                    COLUMN_NAME_DATE +" TEXT, "+
                    COLUMN_NAME_TIMEALARM +" TEXT) ";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
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

    public NoteTable getNoteTable(){
        NoteTable noteTable = new NoteTable(this);
        return noteTable;
    }
}
