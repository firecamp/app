package ca.xef6.app.util;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public abstract class TimePickerFragment extends DialogFragment implements
	OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	final Activity activity = getActivity();
	final Calendar calendar = Calendar.getInstance();
	int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
	int minute = calendar.get(Calendar.MINUTE);
	return new TimePickerDialog(activity, this, hourOfDay, minute,
		DateFormat.is24HourFormat(activity));
    }

    @Override
    public abstract void onTimeSet(TimePicker view, int hourOfDay, int minute);

}