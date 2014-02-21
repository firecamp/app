package ca.xef6.app.util;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public abstract class DatePickerFragment extends DialogFragment implements
	OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	final Activity activity = getActivity();
	final Calendar calendar = Calendar.getInstance();
	int year = calendar.get(Calendar.YEAR);
	int monthOfYear = calendar.get(Calendar.MONTH);
	int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
	return new DatePickerDialog(activity, this, year, monthOfYear,
		dayOfMonth);
    }

    @Override
    public abstract void onDateSet(DatePicker view, int year, int monthOfYear,
	    int dayOfMonth);

}