package nhatan172.noteapp.NoteModel;

import android.provider.BaseColumns;

/**
 * Created by nhata on 14/04/2017.
 */

public final class NoteContract {
    private NoteContract() {}

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_DATE = "update_time";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_TIMEALARM = "time_alarm";
    }
}
