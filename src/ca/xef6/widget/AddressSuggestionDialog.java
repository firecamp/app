package ca.xef6.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AddressSuggestionDialog extends DialogFragment {

    final ArrayAdapter<String> adapter;

    public AddressSuggestionDialog() {
	adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

	final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	builder.setTitle("Please select the nearest location.");

	builder.setNegativeButton("None", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	});

	builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		Toast.makeText(getActivity(), "which = " + which, Toast.LENGTH_SHORT).show();
	    }
	});
	return builder.create();
    }
}
