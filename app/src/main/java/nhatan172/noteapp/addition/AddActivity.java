package nhatan172.noteapp.addition;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.Visibility;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nhatan172.noteapp.general.AlarmReceiver;
import nhatan172.noteapp.NoteModel.NoteDbHelper;
import nhatan172.noteapp.R;
import nhatan172.noteapp.general.StaticMethod;
import nhatan172.noteapp.main.MainActivity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AddActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private LinearLayout ll;
    private String mBackGroundColor = "#ffffff";
    private EditText et_note;
    private EditText et_title;
    private TextView tv_actionbar;
    private NoteDbHelper mNoteDbHelper;
    private TextView tv_alarm;
    private LinearLayout ll_dateTimePicker;
    private ArrayAdapter<CharSequence> adapterTime;
    private ArrayAdapter<CharSequence> adapterDate;
    private ArrayList<String> listTime;
    private ArrayList<String> listDate;
    private Spinner sp_date;
    private Spinner sp_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mNoteDbHelper = new NoteDbHelper(this);

        initView();
        initSpinnerTime();
        initSpinnerDate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TITLE",et_title.getText().toString());
        outState.putString("NOTE",et_note.getText().toString());
        outState.putSerializable("LISTDATE",listDate);
        outState.putSerializable("LISTTIME",listTime);
        outState.putInt("ALRAMSTATUS",tv_alarm.getVisibility());
        outState.putInt("DATEPOST",sp_date.getSelectedItemPosition());
        outState.putInt("TIMEPOST",sp_time.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        et_title.setText(savedInstanceState.getString("TITLE"));
        et_note.setText(savedInstanceState.getString("NOTE"));
        listDate = (ArrayList<String>) savedInstanceState.getSerializable("LISTDATE");
        listTime = (ArrayList<String>) savedInstanceState.getSerializable("LISTTIME");
        int status = savedInstanceState.getInt("ALRAMSTATUS",sp_date.getVisibility());
        adapterDate = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listDate);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTime = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listTime);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_time.setAdapter(adapterTime);
        sp_date.setAdapter(adapterDate);
        sp_date.setSelection( savedInstanceState.getInt("DATEPOST"));
        sp_time.setSelection(savedInstanceState.getInt("TIMEPOST"));
        if(status == View.VISIBLE) {
            ll_dateTimePicker.setVisibility(View.INVISIBLE);
            tv_alarm.setVisibility(View.VISIBLE);
        }
        else{
            ll_dateTimePicker.setVisibility(View.VISIBLE);
            tv_alarm.setVisibility(View.INVISIBLE);
        }
    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.action_bar_add, null);
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
        ll = (LinearLayout) findViewById(R.id.activity_add);
        et_title = (EditText) findViewById(R.id.et_title);
        et_note = (EditText) findViewById(R.id.et_note);
        sp_time = (Spinner) findViewById(R.id.sp_time);
        sp_date = (Spinner) findViewById(R.id.sp_date);
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

        View customPopUp = getLayoutInflater().inflate(R.layout.pop_up_color, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customPopUp);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void changeBackgroud(View v) {
        alertDialog.dismiss();
        ll.setBackgroundColor(Color.parseColor((String) v.getTag()));
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

    public void backActivity(){
        Intent newAct = new Intent(this,MainActivity.class);
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
        String note = StaticMethod.handleString(et_note.getText());
        String title = StaticMethod.handleString(et_title.getText());
        if (title.isEmpty()) {
            if (note.isEmpty())
                return false;
            else
                title = note.substring(0, (note.length() < 25 ? note.length() : 25));
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String time = df.format(Calendar.getInstance().getTime());
        String timeAlarm = "";
        if(ll_dateTimePicker.getVisibility() == View.VISIBLE)
            timeAlarm = getAlarmTime();
        long noteIndex =  mNoteDbHelper.insertNote(title, note, mBackGroundColor, time,timeAlarm);
        if(ll_dateTimePicker.getVisibility() == View.VISIBLE)
            setAlarm(noteIndex,timeAlarm,title);
        mNoteDbHelper.close();
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
    public String getAlarmTime(){
        String time = listTime.get(sp_time.getSelectedItemPosition());
        String date = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        switch (sp_date.getSelectedItemPosition()+1){
            case 1:
                date = df.format(c.getTime());
                break;
            case 2:
                c.add(Calendar.DATE,1);
                date = df.format(c.getTime());
                break;
            case 3:
                c.add(Calendar.DATE,7);
                date = df.format(c.getTime());
                break;
            case 5:
                date = listDate.get(5);
                break;
            default:
                date = df.format(c.getTime());
                break;
        }
        return date + " " + time;
    }
    public void setAlarm(long id,String time,String title) {
        if (!time.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date date = df.parse(time);
                Long alertTime = date.getTime();
                Bundle infoBunlde = new Bundle();
                infoBunlde.putInt("INDEX",(int)id);
                infoBunlde.putString("NOTE",title);
                Intent alertIntent = new Intent(MainActivity.getAppContext(), AlarmReceiver.class);
                alertIntent.putExtras(infoBunlde);
                AlarmManager alarmManager = (AlarmManager) MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(MainActivity.getAppContext(), (int)id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(MainActivity.getAppContext(), (int)id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void initSpinnerTime(){
        listTime = new ArrayList<String>();
        listTime.add("09:00");
        listTime.add("13:00");
        listTime.add("17:00");
        listTime.add("20:00");
        listTime.add("Other...");
        adapterTime = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listTime);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_time.setAdapter(adapterTime);
        sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int currentSelected = 1;
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                if (l == 4) {
                    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int h, int m) {
                            if (listTime.size() > 5)
                                listTime.set(5, StaticMethod.reformTime(h) + ":" + StaticMethod.reformTime(m));
                            else
                                listTime.add(5, StaticMethod.reformTime(h) + ":" + StaticMethod.reformTime(m));
                            adapterTime.notifyDataSetChanged();
                            sp_time.setSelection(6);
                        }
                    };
                    Calendar c = Calendar.getInstance();
                    int h = c.get(Calendar.HOUR_OF_DAY);
                    int m = c.get(Calendar.MINUTE);
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, listener, h, m,
                            true);
                    timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            timePickerDialog.dismiss();
                            sp_time.setSelection(currentSelected);
                        }
                    });
                    timePickerDialog.setTitle("Choose Time");
                    timePickerDialog.show();
                } else currentSelected = sp_time.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initSpinnerDate(){
        listDate = new ArrayList<String>();
        listDate.add("Today");
        listDate.add("Tomorrow");
        listDate.add("Next " + StaticMethod.getCurrentDay());
        listDate.add("Other...");
        adapterDate = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listDate);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_date.setAdapter(adapterDate);
        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int currentSelected = 1;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == 3) {
                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                            if (listDate.size() > 4)
                                listDate.set(4, StaticMethod.reformTime(d) + "/" + StaticMethod.reformTime(m) + "/" + StaticMethod.reformTime(y));
                            else
                                listDate.add(4, StaticMethod.reformTime(d) + "/" + StaticMethod.reformTime(m) + "/" + StaticMethod.reformTime(y));
                            adapterDate.notifyDataSetChanged();
                            sp_date.setSelection(5);
                        }
                    };
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
                    datePickerDialog.setTitle("Choose Date");
                    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            sp_date.setSelection(currentSelected);
                        }
                    });
                    datePickerDialog.show();
                }
                else currentSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}