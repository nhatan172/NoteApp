package nhatan172.noteapp.custom.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

import nhatan172.noteapp.utils.StaticMethod;

/**
 * Created by nhata on 24/05/2017.
 */

public class SpinnerDate extends Spinner {
    private ArrayList<String> mListDate;
    private ArrayAdapter<CharSequence> mAdapterDate;

    public SpinnerDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListDate = new ArrayList<String>();
        mListDate.add("Today");
        mListDate.add("Tomorrow");
        mListDate.add("Next " + StaticMethod.getCurrentDay());
        mListDate.add("Other...");
        mAdapterDate = new ArrayAdapter(context, android.R.layout.simple_spinner_item, mListDate);
        mAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(mAdapterDate);
        SelectedListenner listener = new SelectedListenner();
        this.setOnItemSelectedListener(listener);
    }

    private class SelectedListenner implements AdapterView.OnItemSelectedListener {
        int mCurrentSelected = 1;
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position == 3) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        if (mListDate.size() > 4)
                            mListDate.set(4, StaticMethod.reformTime(d) + "/" + StaticMethod.reformTime(m+1) + "/"
                                    + StaticMethod.reformTime(y));
                        else
                            mListDate.add(4, StaticMethod.reformTime(d) + "/" + StaticMethod.reformTime(m+1) + "/"
                                    + StaticMethod.reformTime(y));
                        mAdapterDate.notifyDataSetChanged();
                        SpinnerDate.this.setSelection(5);
                    }
                };
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
                datePickerDialog.setTitle("Choose Date");
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        SpinnerDate.this.setSelection(mCurrentSelected);
                    }
                });
                datePickerDialog.show();
            }
            else mCurrentSelected = position;
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Do nothing
        }
    }
    public ArrayList<String> getListDate() {
        return mListDate;
    }

    public ArrayAdapter<CharSequence> getAdapterDate() {
        return mAdapterDate;
    }

    public void setListDate(ArrayList<String> listDate) {
        mListDate = listDate;
        mAdapterDate = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mListDate);
        mAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(mAdapterDate);
    }

    public void setNewDate(String date){
        mListDate.add(4, date);
        mAdapterDate.notifyDataSetChanged();
        this.setSelection(5);
    }
}
