package nhatan172.noteapp.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import nhatan172.noteapp.activity.DetailActivity;
import nhatan172.noteapp.custom.view.EditTextDrawLine;
import nhatan172.noteapp.custom.view.SpinnerDate;
import nhatan172.noteapp.custom.view.SpinnerTime;
import nhatan172.noteapp.db.db.table.NoteTable;
import nhatan172.noteapp.notification.AlarmManager;
import nhatan172.noteapp.model.Note;
import nhatan172.noteapp.utils.NoteContent;
import nhatan172.noteapp.db.DatabaseManager;
import nhatan172.noteapp.R;
import nhatan172.noteapp.utils.StaticMethod;


public class PlaceholderFragment extends Fragment {
    /**
     * The fragment for display element in DetailActivity
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private LinearLayout ll_activity ;
    private TextView tv_dateUpdate ;
    private EditTextDrawLine et_note ;
    private EditText et_title ;
    private LinearLayout ll_dateTimePicker;
    private SpinnerTime sp_time;
    private SpinnerDate sp_date;
    private TextView tv_alarm;
    private OnPaperFragmentInteractionListener mListener;
    private ImageView iv_close;
    private Note item;
    private DatabaseManager mDBHelper;
    private int agrs;
    private NoteTable mNoteTable;

    public PlaceholderFragment() {
        // Do nothing
    }
    @Override
    public void onStop() {
        if (!DetailActivity.sDeleteAction)
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        rootView = inflater.inflate(R.layout.item_fragment_detail, container, false);
        initView();
        initViewListener();
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
        // For CallBack action from fragment
        void onPaperFragmentInteraction(Editable et,Editable et2);
    }
    private void initView(){
        ll_activity = (LinearLayout) rootView.findViewById(R.id.fragment_detail);
        tv_dateUpdate = (TextView)rootView.findViewById(R.id.tv_time_created2);
        et_note = (EditTextDrawLine) rootView.findViewById(R.id.et_note2);
        et_title = (EditText)rootView.findViewById(R.id.et_title2);
        tv_dateUpdate.setText( item.getUpdatedTime());
        et_title.setText(item.getTitle());
        iv_close = (ImageView)rootView.findViewById(R.id.iv_close_picker);
        et_note.setText(item.getNote());
        ll_activity.setBackgroundColor(Color.parseColor(item.getColor()));
        ll_activity.setTag(item.getColor());
        ll_dateTimePicker = (LinearLayout)rootView.findViewById(R.id.ll_dateTimePicker2);
        sp_date = (SpinnerDate) rootView.findViewById(R.id.sp_date2);
        sp_time = (SpinnerTime) rootView.findViewById(R.id.sp_time2);
        tv_alarm = (TextView)rootView.findViewById(R.id.tv_alarm2);
        if(item.hasAlarm()) {
            sp_date.setNewDate(item.getTimeAlarm().substring(0,10));
            sp_time.setNewTime(item.getTimeAlarm().substring(11));
            tv_alarm.setVisibility(View.INVISIBLE);
            ll_dateTimePicker.setVisibility(View.VISIBLE);
        }
    }

    private void initViewListener(){
        tv_alarm.setOnClickListener(new ShowDateTimePicker());
        iv_close.setOnClickListener(new CloseDateTimePicker());
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
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
                //Do nothing
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(mListener!=null)
                    mListener.onPaperFragmentInteraction(editable,et_note.getText());
            }
        });
    }

    public boolean saveData() {
        Note newItem = new Note();
        String note = StaticMethod.handleString(et_note.getText());
        String title = StaticMethod.handleString(et_title.getText());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String time = df.format(Calendar.getInstance().getTime());
        String timeAlarm = "";
        if (title.isEmpty())
            if (note.isEmpty())
                return false;
            else
                title = note.substring(0, (note.length() < 25 ? note.length() : 25));
        newItem.setTitle(title);
        newItem.setNote(note);
        newItem.setUpdatedTime(time);
        newItem.setColor((String)ll_activity.getTag());
        newItem.setIndex(item.getIndex());
        if(ll_dateTimePicker.getVisibility() == View.VISIBLE) {
            timeAlarm = getAlarmTime();
            newItem.setTimeAlarm(timeAlarm);
            newItem.setHasAlarm(true);
            if(!item.getTimeAlarm().equals(timeAlarm))
                AlarmManager.setAlarm(item.getIndex(),timeAlarm,title);
        } else {
            newItem.setHasAlarm(false);
            if(item.hasAlarm())
                AlarmManager.cancelAlarm(item.getIndex());
        }
        if (!item.equals(newItem)) {
            mNoteTable.updateNote(newItem);
            NoteContent.sNoteContent.set(agrs, newItem);
        }
        return true;
    }

    public class ShowDateTimePicker implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            tv_alarm.setVisibility(View.INVISIBLE);
            ll_dateTimePicker.setVisibility(View.VISIBLE);
        }
    }

    public class CloseDateTimePicker implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            tv_alarm.setVisibility(View.VISIBLE);
            ll_dateTimePicker.setVisibility(View.INVISIBLE);
        }
    }

    public String getAlarmTime(){
        String time = sp_time.getListTime().get(sp_time.getSelectedItemPosition());
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
                date = sp_date.getListDate().get(4);
                break;
            default:
                date = df.format(c.getTime());
                break;
        }
        return date + " " + time;
    }
}