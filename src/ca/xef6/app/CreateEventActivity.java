package ca.xef6.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import ca.xef6.app.db.ContentProvider;
import ca.xef6.app.util.DatePickerFragment;
import ca.xef6.app.util.TimePickerFragment;

public class CreateEventActivity extends FragmentActivity {

    private EditText         name;
    private EditText         description;
    private Button           date;
    private Button           time;
    private ImageView        image;
    private String           imageUrl;

    private static final int RESULT_LOAD_IMAGE = 1;

    private Uri              eventUri;

    public void createEvent(View view) {
        if (TextUtils.isEmpty(name.getText().toString())) {
            // makeToast();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                                                       filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageUrl = cursor.getString(columnIndex);
            cursor.close();

            image.setImageBitmap(BitmapFactory.decodeFile(imageUrl));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        date = (Button) findViewById(R.id.date);
        time = (Button) findViewById(R.id.time);
        image = (ImageView) findViewById(R.id.image);
        Bundle extras = getIntent().getExtras();

        // Check from the saved Instance
        eventUri = (savedInstanceState == null) ? null
                : (Uri) savedInstanceState.getParcelable(ContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            eventUri = extras
                    .getParcelable(ContentProvider.CONTENT_ITEM_TYPE);

            fillData(eventUri);
        }
    }

    public void showTimePickerDialog(View view) {
        final Button button = (Button) view;
        new TimePickerFragment() {

            @Override
            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                button.setText(hourOfDay + ":" + minute);
            }

        }.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        final Button button = (Button) view;
        new DatePickerFragment() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                button.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }

        }.show(getSupportFragmentManager(), "datePicker");
    }

    public void showImagePickerDialog(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void fillData(Uri uri) { // TODO
        /*String[] projection = EventsTable.ALL_COLUMNS_NO_ID;
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                                                   null);
        if (cursor != null) {
            cursor.moveToFirst();

            name.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(EventsTable.COLUMN_NAME)));
            description.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(EventsTable.COLUMN_DESCRIPTION)));
            date.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(EventsTable.COLUMN_DATE)));
            time.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(EventsTable.COLUMN_TIME)));
            imageUrl = cursor.getString(cursor
                    .getColumnIndexOrThrow(EventsTable.COLUMN_IMAGE_URL));
            image.setImageBitmap(BitmapFactory.decodeFile(imageUrl));

            cursor.close();
        }*/
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(ContentProvider.CONTENT_ITEM_TYPE, eventUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String dataName = name.getText().toString();
        String dataDescription = description.getText().toString();
        String dataDate = date.getText().toString();
        String dataTime = time.getText().toString();
        if (dataName.length() == 0 || dataDescription.length() == 0
                || dataDate.length() == 0 || dataTime.length() == 0
                || imageUrl.length() == 0) {
            return;
        }
        ContentValues values = new ContentValues();
        // TODO
        /*values.put(EventsTable.COLUMN_NAME, dataName);
        values.put(EventsTable.COLUMN_AUTHOR, "none");
        values.put(EventsTable.COLUMN_DESCRIPTION, dataDescription);
        values.put(EventsTable.COLUMN_DATE, dataDate);
        values.put(EventsTable.COLUMN_TIME, dataTime);
        values.put(EventsTable.COLUMN_IMAGE_URL, imageUrl);*/

        if (eventUri == null) {
            eventUri = getContentResolver().insert(ContentProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(eventUri, values, null, null);
        }
    }

}
