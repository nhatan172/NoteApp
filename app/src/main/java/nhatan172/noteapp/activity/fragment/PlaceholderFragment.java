package nhatan172.noteapp.activity.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nhatan172.noteapp.activity.DetailActivity;
import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.notification.AlarmReceiver;
import nhatan172.noteapp.model.Note;
import nhatan172.noteapp.utils.NoteContent;
import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.R;
import nhatan172.noteapp.utils.StaticMethod;
import nhatan172.noteapp.activity.MainActivity;


public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private LinearLayout ll_activity ;
    private TextView tv_dateUpdate ;
    private EditText et_note ;
    private EditText et_title ;
    private LinearLayout ll_dateTimePicker;
    private Spinner sp_time;
    private Spinner sp_date;
    private TextView tv_alarm;
    private OnPaperFragmentInteractionListener mListener;
    private ArrayAdapter<CharSequence> adapterTime;
    private ArrayAdapter<CharSequence> adapterDate;
    private ArrayList<String> listTime;
    private ArrayList<String> listDate;
    private ImageView iv_close;
    private Note item;
    private DatabaseManager mDBHelper;
    private int agrs;
    private NoteTable mNoteTable;
    public PlaceholderFragment() {
    }

    @Override
    public void onStop() {
        saveData();
        super.onStop();
    }


    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DetailActivity detailActivity = (DetailActivity)getContext();
        mDBHelper = detailActivity.getmDBHelper();
        mNoteTable = mDBHelper.getNoteTable();
        agrs = getArguments().getInt(ARG_SECTION_NUMBER);
        item = NoteContent.sNoteContent.get(agrs);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        initView();

        initSpinnerTime();
        initSpinnerDate();
        initViewListener();
        if(item.hasAlarm()){
            listDate.add(4,item.getTimeAlarm().substring(0,10) );
            adapterDate.notifyDataSetChanged();
            sp_date.setSelection(5);
            listTime.add(5, item.getTimeAlarm().substring(11));
            adapterTime.notifyDataSetChanged();
            sp_time.setSelection(6);
            tv_alarm.setVisibility(View.INVISIBLE);
            ll_dateTimePicker.setVisibility(View.VISIBLE);
        }
        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaperFragmentInteractionListener) {
            mListener = (OnPaperFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnPaperFragmentInteractionListener) {
            mListener = (OnPaperFragmentInteractionListener) activity;

        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnPaperFragmentInteractionListener {

        void onPaperFragmentInteraction(Editable et,Editable et2);

    }
    private void initView(){
        ll_activity = (LinearLayout) rootView.findViewById(R.id.fragment_detail);
        tv_dateUpdate = (TextView)rootView.findViewById(R.id.tv_time_created2);
        et_note = (EditText)rootView.findViewById(R.id.et_note2);
        et_title = (EditText)rootView.findViewById(R.id.et_title2);
        tv_dateUpdate.setText( item.getUpdatedTime());
        et_title.setText(item.getTitle());
        iv_close = (ImageView)rootView.findViewById(R.id.iv_close_picker);
        et_note.setText(item.getNote());
        ll_activity.setBackgroundColor(Color.parseColor(item.getColor()));
        ll_activity.setTag(item.getColor());
        ll_dateTimePicker = (LinearLayout)rootView.findViewById(R.id.ll_dateTimePicker2);
        sp_date = (Spinner)rootView.findViewById(R.id.sp_date2);
        sp_time = (Spinner)rootView.findViewById(R.id.sp_time2);
        tv_alarm = (TextView)rootView.findViewById(R.id.tv_alarm2);
    }

    private void initSpinnerTime(){
        listTime = new ArrayList<String>();
        listTime.add("09:00");
        listTime.add("13:00");
        listTime.add("17:00");
        listTime.add("20:00");
        listTime.add("Other...");
        adapterTime = new ArrayAdapter(getContext(),
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
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), TimePickerDialog.THEME_HOLO_LIGHT, listener, h, m,
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
    private void initSpinnerDate(){
        listDate = new ArrayList<String>();
        listDate.add("Today");
        listDate.add("Tomorrow");
        listDate.add("Next " + StaticMethod.getCurrentDay());
        listDate.add("Other...");
        adapterDate = new ArrayAdapter(getContext(),
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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
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


    private void initViewListener(){
        tv_alarm.setOnClickListener(new ShowDateTimePicker());
        iv_close.setOnClickListener(new CloseDateTimePicker());
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(mListener!=null)
                    mListener.onPaperFragmentInteraction(editable,et_note.getText());
            }
        });
        et_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(mListener!=null)
                    mListener.onPaperFragmentInteraction(editable,et_note.getText());
            }
        });
    }

    public class ShowDateTimePicker  implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            tv_alarm.setVisibility(View.INVISIBLE);
            ll_dateTimePicker.setVisibility(View.VISIBLE);
        }
    }

    public class CloseDateTimePicker  implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            tv_alarm.setVisibility(View.VISIBLE);
            ll_dateTimePicker.setVisibility(View.INVISIBLE);
        }
    }

    public boolean saveData(){
        Note newItem = new Note();
        String note = StaticMethod.handleString(et_note.getText());
        String title = StaticMethod.handleString(et_title.getText());
        if (title.isEmpty()) {
            if (note.isEmpty())
                return false;
            else
                title = note.substring(0, (note.length() < 25 ? note.length() : 25));
        }
        newItem.setTitle(title);
        newItem.setNote(note);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String time = df.format(Calendar.getInstance().getTime());
        newItem.setUpdatedTime(time);
        newItem.setColor((String)ll_activity.getTag());
        newItem.setIndex(item.getIndex());
        String timeAlarm = "";
        if(ll_dateTimePicker.getVisibility() == View.VISIBLE) {
            timeAlarm = getAlarmTime();
            newItem.setTimeAlarm(timeAlarm);
            newItem.setHasAlarm(true);
            if(!item.getTimeAlarm().equals(timeAlarm))
                setAlarm(item.getIndex(),timeAlarm,title);
        }
        else{
            newItem.setHasAlarm(false);
            if(item.hasAlarm())
                cancelAlarm(item.getIndex());
        }
        mNoteTable.updateNote(newItem);
        NoteContent.sNoteContent.set(agrs,newItem);
        return true;
    }

    private void cancelAlarm(int index) {
        Intent alertIntent = new Intent(MainActivity.getAppContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel( PendingIntent.getBroadcast(MainActivity.getAppContext(),index, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    public void setAlarm(int id,String time,String title) {
        if (!time.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date date = df.parse(time);
                Long alertTime = date.getTime();
                Bundle infoBunlde = new Bundle();
                infoBunlde.putInt("INDEX",id);
                infoBunlde.putString("NOTE",title);
                Intent alertIntent = new Intent(MainActivity.getAppContext(), AlarmReceiver.class);
                alertIntent.putExtras(infoBunlde);
                AlarmManager alarmManager = (AlarmManager)MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(MainActivity.getAppContext(), id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(MainActivity.getAppContext(), id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
                date = listDate.get(4);
                break;
            default:
                date = df.format(c.getTime());
                break;
        }
        return date + " " + time;
    }
}