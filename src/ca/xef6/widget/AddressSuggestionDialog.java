package ca.xef6.widget;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import ca.xef6.app.util.ReverseGeocoder;

import com.google.android.gms.maps.model.LatLng;

public class AddressSuggestionDialog extends DialogFragment {

    public static interface Callback {
	void onAddressSelected(String selectedAddress);
    }

    private ArrayAdapter<String> adapter;
    private Callback	     callback;
    private LatLng	       position;

    public void setPosition(LatLng position) {
	this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

	adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);

	ReverseGeocoder.getAddressSuggestionsAsync(position, true, new ReverseGeocoder.Callback() {
	    @Override
	    public void onAddressSuggestionsLoaded(List<String> addressSuggestions) {
		adapter.addAll(addressSuggestions);
	    }
	});

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
		Toast.makeText(getActivity(), "which = " + which + ", item = " + adapter.getItem(which), Toast.LENGTH_SHORT).show();
		if (callback != null) {
		    callback.onAddressSelected(adapter.getItem(which));
		}
	    }
	});
	return builder.create();
    }

    public void setCallback(Callback callback) {
	this.callback = callback;
    }
}
