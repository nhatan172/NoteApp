package nhatan172.noteapp.activity.activity.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.utils.NoteContent;

/**
 * Created by nhata on 24/05/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected DatabaseManager mDatabaseManager;
    protected final String MODE_ARG = "MODE";
    protected final String POSITION_ARG = "ItemPosition";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseManager = new DatabaseManager(this);
        NoteContent noteContent = new NoteContent(mDatabaseManager);
        noteContent.getNoteContent();
        setActionBar();
    }
    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
