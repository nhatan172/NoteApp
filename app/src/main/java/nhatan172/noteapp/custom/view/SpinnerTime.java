package nhatan172.noteapp.custom.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import nhatan172.noteapp.utils.StaticMethod;

/**
 * Created by nhata on 12/05/2017.
 */

public class SpinnerTime extends Spinner {
    private ArrayList<String> mListTime;
    private ArrayAdapter<CharSequence> mAdapterTime;

    public SpinnerTime(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListTime = new ArrayList<String>();
        mListTime.add("09:00");
        mListTime.add("13:00");
        mListTime.add("17:00");
        mListTime.add("20:00");
        mListTime.add("Other...");
        mAdapterTime = new ArrayAdapter(context, android.R.layout.simple_spinner_item, mListTime);
        mAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(mAdapterTime);
        SelectedListenner listener = new SelectedListenner();
        this.setOnItemSelectedListener(listener);
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        return super.onCreateDrawableState(extraSpace);
    }

    private class SelectedListenner implements OnItemSelectedListener{
        int mCurrentSelected = 1;
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position == 4) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        if (mListTime.size() > 5)
                            mListTime.set(5, StaticMethod.reformTime(h) + ":" + StaticMethod.reformTime(m));
                        else
                            mListTime.add(5, StaticMethod.reformTime(h) + ":" + StaticMethod.reformTime(m));
                        mAdapterTime.notifyDataSetChanged();
                        SpinnerTime.this.setSelection(6);
                    }
                };
                Calendar c = Calendar.getInstance();
                int h = c.get(Calendar.HOUR_OF_DAY);
                int m = c.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        TimePickerDialog.THEME_HOLO_LIGHT, listener, h, m, true);
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        timePickerDialog.dismiss();
                        SpinnerTime.this.setSelection(mCurrentSelected);
                    }
                });
                timePickerDialog.setTitle("Choose Time");
                timePickerDialog.show();
            } else mCurrentSelected = SpinnerTime.this.getSelectedItemPosition();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Do nothing
        }
    }
    public ArrayList<String> getListTime() {
        return mListTime;
    }

    public ArrayAdapter<CharSequence> getAdapterTime() {
        return mAdapterTime;
    }

    public void setListTime(ArrayList<String> listTime) {
        mListTime = listTime;
        mAdapterTime = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mListTime);
        mAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(mAdapterTime);
    }
    public void setNewTime(String time){
        mListTime.add(5, time);
        mAdapterTime.notifyDataSetChanged();
        this.setSelection(6);
    }
}
