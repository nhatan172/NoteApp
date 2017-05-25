package nhatan172.noteapp.activity.activity.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import nhatan172.noteapp.R;
import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.utils.NoteContent;

public class BaseActivity extends AppCompatActivity {
    protected DatabaseManager mDatabaseManager;
    protected final String MODE_ARG = "MODE";
    protected final String POSITION_ARG = "ItemPosition";
    protected final String TITLE_ARG = "TITLE";
    protected final String NOTE_ARG = "NOTE";
    protected final String LISTDATE_ARG = "ListDate";
    protected final String LISTTIME_ARG = "ListTime";
    protected final String STATUS_ARG = "ALRAMSTATUS";
    protected final String TIMEPOST_ARG ="TIMEPOST";
    protected final String DATEPOST_ARG = "DATEPOST";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
            else {
                Toast toast = Toast.makeText(this,"You don't have permission for using Camera",Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
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
    @Override
    protected void onStop() {
        mDatabaseManager.close();
        super.onStop();
    }
}
