package nhatan172.noteapp.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import  android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar;

import nhatan172.noteapp.activity.activity.base.BaseActivity;
import nhatan172.noteapp.R;
import nhatan172.noteapp.activity.fragment.NoteFragment;

public class MainActivity extends BaseActivity implements NoteFragment.OnListFragmentInteractionListener {
    private static Context sContext;
    private NoteFragment mNoteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();

        if(savedInstanceState == null) {
            mNoteFragment = NoteFragment.newInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.note_fragment, mNoteFragment).commit();
        }
    }
    @Override
    public void onListFragmentInteraction(int position) {
        Bundle positionBunlde = new Bundle();
        positionBunlde.putInt(MODE_ARG, 1);
        //Define bundle from Main or Notification = 2
        positionBunlde.putInt(POSITION_ARG, position);
        Intent newActivity =  new Intent(this, DetailActivity.class);
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

    public static Context getAppContext(){
        return MainActivity.sContext;
    }

    public void goAddActivity(View clickedButton){
        Intent newActivity = new Intent(this, AdditionActivity.class);
        startActivity(newActivity);
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.item_main_bar, null);
        actionBar.setCustomView(customNav, lp1);
        Toolbar toolbar = (Toolbar) customNav.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
    }
}
