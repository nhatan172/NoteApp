package nhatan172.noteapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.graphics.PorterDuff;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.model.Note;
import nhatan172.noteapp.custom.view.DetailPager;
import nhatan172.noteapp.activity.fragment.PlaceholderFragment;
import nhatan172.noteapp.custom.adapter.SectionsPagerAdapter;
import nhatan172.noteapp.notification.AlarmReceiver;
import nhatan172.noteapp.utils.NoteContent;
import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.R;
import nhatan172.noteapp.utils.StaticMethod;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DetailActivity extends AppCompatActivity implements PlaceholderFragment.OnPaperFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DetailPager mDetailPager;
    private ImageView iv_next;
    private ImageView iv_previous;
    private  TextView tv_actionbar;
    private AlertDialog alertDialog;
    private DatabaseManager mDBHelper;
    private String share = "Note";
    private int position = 0;
    private NoteTable mNoteTable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);

        initActoionBar();
        initFragmentPaper();
        mDBHelper = new DatabaseManager(this);
        mNoteTable = mDBHelper.getNoteTable();
        NoteContent noteContent = new NoteContent(mDBHelper);
        noteContent.getNoteContent();

        settingPosition();


    }
    public void settingPosition(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            int mode = bundle.getInt("MODE");
            if(mode == 1)
                position = bundle.getInt("ItemPosition");
            else {

                position = bundle.getInt("ItemPosition")-1;
                iv_next.setSelected(false);
                iv_next.setClickable(false);
                iv_previous.setSelected(false);
                iv_previous.setClickable(false);

            }
        }
    }
    private void initFragmentPaper(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mDetailPager = (DetailPager) findViewById(R.id.container);
        mDetailPager.setAdapter(mSectionsPagerAdapter);


        mDetailPager.setCurrentItem(position);

        mDetailPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Note note = NoteContent.sNoteContent.get(mDetailPager.getCurrentItem());
                tv_actionbar.setText(note.getTitle());
                share = note.getTitle()+"\n"+note.getNote();
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initActoionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.action_bar_detail, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        actionBar.setCustomView(customNav, lp1);
        tv_actionbar = (TextView) customNav.findViewById(R.id.tv_actionbar);
        Toolbar toolbar = (Toolbar) customNav.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
    }
    public void nextPage(View v) {
        int i = mDetailPager.getCurrentItem();
        if (i < mSectionsPagerAdapter.getCount())
            mDetailPager.setCurrentItem(i+1);
        if(i == mSectionsPagerAdapter.getCount()-1) {
            iv_next.setClickable(false);
            iv_next.setColorFilter(Color.parseColor("#a6a6a6"), PorterDuff.Mode.MULTIPLY);
        }
        iv_previous.setClickable(true);
        iv_previous.clearColorFilter();
    }

    public void previousPage(View v){
        int i = mDetailPager.getCurrentItem();
        if(i>0)
            mDetailPager.setCurrentItem(i-1);
        if(i == 1){
            iv_previous.setClickable(false);
            iv_previous.setColorFilter(Color.parseColor("#a6a6a6"), PorterDuff.Mode.MULTIPLY);
    }
    iv_next.setClickable(true);
    iv_next.clearColorFilter();
    }


    public void deleteNote(View v){
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Are you sure you want delete this");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int index = NoteContent.sNoteContent.get(mDetailPager.getCurrentItem()).getIndex();
                mNoteTable.deleteNote(index);
                if(NoteContent.sNoteContent.get(mDetailPager.getCurrentItem()).hasAlarm())
                    cancelAlarm(index);
                mDBHelper.close();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    public void shareNote(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,share);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,"Share with"));
    }
    public void updateNote(View v){
        this.onStop();
        Intent newActivity = new Intent(this, MainActivity.class);
        startActivity(newActivity);
    }

    @Override
    public void onPaperFragmentInteraction(Editable et,Editable et2) {
        String s = StaticMethod.handleString(et);
        String s2 = StaticMethod.handleString(et2);
        if(s.length()!=0)
            tv_actionbar.setText(s);
        else tv_actionbar.setText("Note");
        share = s+"\n"+s2;
    }
    public void popUpColor(View clickedButton){

        View customPopUp = getLayoutInflater().inflate(R.layout.pop_up_color,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customPopUp);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void changeBackgroud(View v) {
        alertDialog.dismiss();
        mDetailPager.getRootView().findViewById(R.id.fragment_detail).setBackgroundColor(Color.parseColor((String)v.getTag()));
        mDetailPager.getRootView().findViewById(R.id.fragment_detail).setTag(((String)v.getTag()));
    }
    public void getCamera(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
    public void newNote(View v){
        PopupMenu popup = new PopupMenu(DetailActivity.this,v);
        popup.getMenuInflater()
                .inflate(R.menu.menu_detail, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent newIntent = new Intent(DetailActivity.this, AdditionActivity.class);
                DetailActivity.this.onStop();
                startActivity(newIntent);
                return true;
            }
        });

        popup.show();
    }
    public void backAction(View v){
        backActivity();
    }

    public DatabaseManager getmDBHelper() {
        return mDBHelper;
    }
    private void cancelAlarm(int index) {
        Intent alertIntent = new Intent(MainActivity.getAppContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel( PendingIntent.getBroadcast(MainActivity.getAppContext(), index, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT));
    }
    public void backActivity(){
        this.onStop();
        Intent newAct = new Intent(this,MainActivity.class);
        startActivity(newAct);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
