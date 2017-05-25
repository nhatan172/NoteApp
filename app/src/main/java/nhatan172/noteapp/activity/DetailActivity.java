package nhatan172.noteapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import nhatan172.noteapp.R;
import nhatan172.noteapp.activity.activity.base.BaseActivity;
import nhatan172.noteapp.activity.fragment.PlaceholderFragment;
import nhatan172.noteapp.custom.adapter.SectionsPagerAdapter;
import nhatan172.noteapp.custom.view.DetailPager;
import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.model.Note;
import nhatan172.noteapp.notification.AlarmManager;
import nhatan172.noteapp.utils.NoteContent;
import nhatan172.noteapp.utils.StaticMethod;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DetailActivity extends BaseActivity implements PlaceholderFragment.OnPaperFragmentInteractionListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DetailPager mDetailPager;
    private ImageView iv_next;
    private ImageView iv_previous;
    private TextView tv_actionbar;
    private Dialog mDialog;
    private String mShareText = "Note";
    private int mPosition = 0;
    private NoteTable mNoteTable ;
    public static boolean sDeleteAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        mNoteTable = mDatabaseManager.getNoteTable();
        initActoionBar();
        initFragmentPaper();
        initPosition();
    }

    public void initPosition() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            int mode = bundle.getInt(MODE_ARG);
            if(mode == 1) {
                mPosition = bundle.getInt(POSITION_ARG);
                if (mPosition == 0)
                    disableClickable(iv_previous);
                if  (mPosition == (mSectionsPagerAdapter.getCount() - 1))
                    disableClickable(iv_next);
            }
            else {
                int index = bundle.getInt(POSITION_ARG);
                mPosition =  NoteContent.getPosition(index);
                disableClickable(iv_next);
                disableClickable(iv_previous);
            }
        }
        mDetailPager.setCurrentItem(mPosition);
    }
    private void disableClickable(ImageView imageView) {
        imageView.setClickable(false);
        imageView.setColorFilter(Color.parseColor("#81D4FA"), PorterDuff.Mode.MULTIPLY);
    }
    private void enableClickable(ImageView imageView){
        imageView.setClickable(true);
        imageView.clearColorFilter();
    }
    private void initFragmentPaper() {
        sDeleteAction = false;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mDetailPager = (DetailPager) findViewById(R.id.container);
        mDetailPager.setAdapter(mSectionsPagerAdapter);
        mDetailPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Note note = NoteContent.sNoteContent.get(mDetailPager.getCurrentItem());
                tv_actionbar.setText(note.getTitle());
                mShareText = note.getTitle()+"\n"+note.getNote();
            }
            @Override
            public void onPageSelected(int position) {
                //Do nothing
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //Do nothing
            }
        });
    }

    private void initActoionBar() {
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.item_detail_bar, null);
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
        int length = mSectionsPagerAdapter.getCount()-2;
        if (i == length)
            disableClickable(iv_next);
        enableClickable(iv_previous);
    }

    public void previousPage(View v) {
        int i = mDetailPager.getCurrentItem();
        if(i>0)
            mDetailPager.setCurrentItem(i-1);
        if(i == 1)
            disableClickable(iv_previous);
        enableClickable(iv_next);
    }

    public void deleteNote(View v) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Are you sure you want delete this");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int index = NoteContent.sNoteContent.get(mDetailPager.getCurrentItem()).getIndex();
                if(NoteContent.sNoteContent.get(mDetailPager.getCurrentItem()).hasAlarm())
                    AlarmManager.cancelAlarm(index);
                mNoteTable.deleteNote(index);
                sDeleteAction = true;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void shareNote(View v) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share with"));
    }

    public void updateNote(View v) {
        this.onStop();
        Intent newActivity = new Intent(this, MainActivity.class);
        startActivity(newActivity);
    }
    @Override
    public void onPaperFragmentInteraction(Editable et, Editable et2) {
        String titleText = StaticMethod.handleString(et);
        String noteText = StaticMethod.handleString(et2);
        if(titleText.length() != 0)
            tv_actionbar.setText(titleText);
        else
            tv_actionbar.setText("Note");
        mShareText = titleText + "\n" + noteText;
    }

    public void popUpColor(View clickedButton) {
        View customPopUp = getLayoutInflater().inflate(R.layout.dialog_select_color, null);
        mDialog = new Dialog(this);
        mDialog.setContentView(customPopUp);
        mDialog.show();
    }

    public void changeBackgroud(View v) {
        mDialog.dismiss();
        mDetailPager.getRootView().findViewById(R.id.fragment_detail)
                .setBackgroundColor(Color.parseColor((String)v.getTag()));
        mDetailPager.getRootView().findViewById(R.id.fragment_detail).setTag(v.getTag());
    }

    public void getCamera(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        5);
            } else {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        }
    }

    public void newNote(View v) {
        PopupMenu popup = new PopupMenu(DetailActivity.this, v);
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
        return mDatabaseManager;
    }

    public void backActivity(){
        this.onStop();
        Intent newAct = new Intent(this, MainActivity.class);
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
