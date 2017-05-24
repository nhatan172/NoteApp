package nhatan172.noteapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import nhatan172.noteapp.activity.activity.base.BaseActivity;
import nhatan172.noteapp.custom.view.SpinnerDate;
import nhatan172.noteapp.custom.view.SpinnerTime;
import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.model.Note;
import nhatan172.noteapp.R;
import nhatan172.noteapp.notification.AlarmManager;
import nhatan172.noteapp.utils.StaticMethod;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AdditionActivity extends BaseActivity {
    private final String TITLE_ARG = "TITLE";
    private final String NOTE_ARG = "NOTE";
    private final String LISTDATE_ARG = "ListDate";
    private final String LISTTIME_ARG = "ListTime";
    private final String STATUS_ARG = "ALRAMSTATUS";
    private final String TIMEPOST_ARG ="TIMEPOST";
    private final String DATEPOST_ARG = "DATEPOST";
    private AlertDialog mAlertDialog;
    private LinearLayout ll_activity;
    private String mBackGroundColor = "#ffffff";
    private EditText et_note;
    private EditText et_title;
    private TextView tv_actionbar;
    private TextView tv_alarm;
    private LinearLayout ll_dateTimePicker;
    private ArrayAdapter<CharSequence> mAdapterTime;
    private ArrayAdapter<CharSequence> mAdapterDate;
    private ArrayList<String> mListTime;
    private ArrayList<String> mListDate;
    private SpinnerDate sp_date;
    private SpinnerTime sp_time;
    private NoteTable mNoteTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mNoteTable = mDatabaseManager.getNoteTable();
        initView();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE_ARG, et_title.getText().toString());
        outState.putString(NOTE_ARG, et_note.getText().toString());
        outState.putSerializable(LISTDATE_ARG, sp_date.getListDate());
        outState.putSerializable(LISTTIME_ARG, sp_time.getListTime());
        outState.putInt(STATUS_ARG, tv_alarm.getVisibility());
        outState.putInt(DATEPOST_ARG, sp_date.getSelectedItemPosition());
        outState.putInt(TIMEPOST_ARG, sp_time.getSelectedItemPosition());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        et_title.setText(savedInstanceState.getString(TITLE_ARG));
        et_note.setText(savedInstanceState.getString(NOTE_ARG));
        mListDate = (ArrayList<String>) savedInstanceState.getSerializable(LISTDATE_ARG);
        mListTime = (ArrayList<String>) savedInstanceState.getSerializable(LISTTIME_ARG);
        int status = savedInstanceState.getInt(STATUS_ARG, sp_date.getVisibility());
        sp_date.setListDate(mListDate);
        sp_time.setListTime(mListTime);
        sp_date.setSelection( savedInstanceState.getInt(DATEPOST_ARG));
        sp_time.setSelection(savedInstanceState.getInt(TIMEPOST_ARG));
        if(status == View.VISIBLE) {
            ll_dateTimePicker.setVisibility(View.INVISIBLE);
            tv_alarm.setVisibility(View.VISIBLE);
        } else {
            ll_dateTimePicker.setVisibility(View.VISIBLE);
            tv_alarm.setVisibility(View.INVISIBLE);
        }
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.item_addition_bar, null);
        tv_actionbar = (TextView) customNav.findViewById(R.id.tv_actionbar);
        actionBar.setCustomView(customNav, lp1);
        Toolbar toolbar = (Toolbar) customNav.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
    }
    public void initView() {
        initActionBar();
        TextView tv_time = (TextView) findViewById(R.id.tv_time_created);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tv_time.setText(date);
        ll_dateTimePicker = (LinearLayout) findViewById(R.id.ll_dateTimePicker);
        tv_alarm = (TextView) findViewById(R.id.tv_alarm);
        ll_activity = (LinearLayout) findViewById(R.id.activity_add);
        et_title = (EditText) findViewById(R.id.et_title);
        et_note = (EditText) findViewById(R.id.et_note);
        sp_time = (SpinnerTime) findViewById(R.id.sp_time);
        sp_date = (SpinnerDate) findViewById(R.id.sp_date);
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String s = StaticMethod.handleString(editable);
                if (s.length() != 0)
                    tv_actionbar.setText(s);
                else tv_actionbar.setText("Note");
            }
        });
    }

    public void popUpColor(View clickedButton) {

        View customPopUp = getLayoutInflater().inflate(R.layout.dialog_select_color, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customPopUp);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    public void changeBackgroud(View v) {
        mAlertDialog.dismiss();
        ll_activity.setBackgroundColor(Color.parseColor((String) v.getTag()));
        mBackGroundColor = (String) v.getTag();

    }
    public void insertNote(View v) {
        saveData();
        backActivity();
    }

    public void getCamera(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    public void backActivity() {
        Intent newAct = new Intent(this, MainActivity.class);
        startActivity(newAct);
    }

    public void backAction(View v) {
        backActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean saveData() {
        String noteText = StaticMethod.handleString(et_note.getText());
        String title = StaticMethod.handleString(et_title.getText());
        Note note = new Note();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String time = df.format(Calendar.getInstance().getTime());
        String timeAlarm = "";
        if (title.isEmpty()) {
            if (noteText.isEmpty())
                return false;
            else
                title = noteText.substring(0, (noteText.length() < 25 ? noteText.length() : 25));
        }
        if(ll_dateTimePicker.getVisibility() == View.VISIBLE)
            timeAlarm = getAlarmTime();
        note.setTitle(title);
        note.setNote(noteText);
        note.setColor(mBackGroundColor);
        note.setUpdatedTime(time);
        note.setTimeAlarm(timeAlarm);
        long noteIndex = mNoteTable.insertNote(note);
        if(ll_dateTimePicker.getVisibility() == View.VISIBLE)
            AlarmManager.setAlarm((int)noteIndex, timeAlarm, title);
        mDatabaseManager.close();
        return true;
    }

    public void showDateTimePicker(View v) {
        tv_alarm.setVisibility(View.INVISIBLE);
        ll_dateTimePicker.setVisibility(View.VISIBLE);
    }

    public void closeDateTimePicker(View v) {
        tv_alarm.setVisibility(View.VISIBLE);
        ll_dateTimePicker.setVisibility(View.INVISIBLE);
    }
    public String getAlarmTime() {
        String time = sp_time.getListTime().get(sp_time.getSelectedItemPosition());
        String date = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        switch (sp_date.getSelectedItemPosition() + 1) {
        case 1:
            date = df.format(c.getTime());
            break;
        case 2:
            c.add(Calendar.DATE, 1);
            date = df.format(c.getTime());
            break;
        case 3:
            c.add(Calendar.DATE, 7);
            date = df.format(c.getTime());
            break;
        case 5:
            date = sp_date.getListDate().get(4);
            break;
        default:
            date = df.format(c.getTime());
            break;
        }
        return date + " " + time;
    }
}