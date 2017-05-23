package nhatan172.noteapp.activity.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by nhata on 12/05/2017.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.setTitle("Choose Time");
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String format  = "%d:%d";

       this.getDialog().setTitle(String.format(format,i,i1));
    }

}
