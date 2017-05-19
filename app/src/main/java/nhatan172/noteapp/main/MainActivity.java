package nhatan172.noteapp.main;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import  android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar;

import nhatan172.noteapp.addition.AddActivity;
import nhatan172.noteapp.detail.DetailActivity;
import nhatan172.noteapp.NoteModel.NoteContent;
import nhatan172.noteapp.NoteModel.NoteDbHelper;
import nhatan172.noteapp.R;

public class MainActivity extends AppCompatActivity implements NoteFragment.OnListFragmentInteractionListener{
    private static Context context;
    private NoteFragment bf;
    public static Context getAppContext(){
        return MainActivity.context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBar();

        NoteDbHelper mNoteDbHelper = new NoteDbHelper(this);
        NoteContent noteContent = new NoteContent(mNoteDbHelper);
        noteContent.getNoteContent();
        if(savedInstanceState == null) {
            bf = NoteFragment.newInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.note_fragment, bf).commit();
        }
    }
    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.action_bar, null);
        actionBar.setCustomView(customNav, lp1);

    }

    public void goAddActivity(View clickedButton){
        Intent newActivity = new Intent(this, AddActivity.class);
        startActivity(newActivity);
    }

    @Override
    public void onListFragmentInteraction(int position) {
        Bundle positionBunlde = new Bundle();
        positionBunlde.putInt("MODE",1);
        //Define bundle from Main or Notification = 2
        positionBunlde.putInt("ItemPosition",position);
        Intent newActivity =  new Intent(this,DetailActivity.class);
        newActivity.putExtras(positionBunlde);
        startActivity(newActivity);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
