package ca.xef6.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import ca.xef6.app.ui.FragmentActivity;
import ca.xef6.app.util.DatePickerFragment;
import ca.xef6.app.util.TimePickerFragment;

import com.facebook.Session;
import com.facebook.SessionState;

public class CreateEventActivity extends FragmentActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    public void createEvent(View view) {
        /*if (TextUtils.isEmpty(name.getText().toString())) {
            // makeToast();
        } else {
            setResult(RESULT_OK);
            finish();
        }*/
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

    private void initialize() {

        setContentView(R.layout.create_event);
        /* name = (EditText) findViewById(R.id.name);
         description = (EditText) findViewById(R.id.description);
         date = (Button) findViewById(R.id.date);
         time = (Button) findViewById(R.id.time);
         image = (ImageView) findViewById(R.id.image);*/
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
            // imageUrl = cursor.getString(columnIndex);
            cursor.close();

            // image.setImageBitmap(BitmapFactory.decodeFile(imageUrl));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        // Check from the saved Instance
        // eventUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(ContentProvider.CONTENT_ITEM_TYPE);
        Bundle extras = getIntent().getExtras();
        // Or passed from the other activity
        if (extras != null) {
            //eventUri = extras.getParcelable(ContentProvider.CONTENT_ITEM_TYPE);
            //  fillData(eventUri);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveState();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        //  outState.putParcelable(ContentProvider.CONTENT_ITEM_TYPE, eventUri);
    }

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
    }

    private void saveState() {
        /* String dataName = name.getText().toString();
         String dataDescription = description.getText().toString();
         String dataDate = date.getText().toString();
         String dataTime = time.getText().toString();
         if (dataName.length() == 0 || dataDescription.length() == 0
                 || dataDate.length() == 0 || dataTime.length() == 0
                 || imageUrl.length() == 0) {
             return;
         }*/
        /*ContentValues values = new ContentValues();
        // TODO
        values.put(EventsTable.COLUMN_NAME, dataName);
        values.put(EventsTable.COLUMN_AUTHOR, "none");
        values.put(EventsTable.COLUMN_DESCRIPTION, dataDescription);
        values.put(EventsTable.COLUMN_DATE, dataDate);
        values.put(EventsTable.COLUMN_TIME, dataTime);
        values.put(EventsTable.COLUMN_IMAGE_URL, imageUrl);

        if (eventUri == null) {
            eventUri = getContentResolver().insert(ContentProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(eventUri, values, null, null);
        }*/
    }

    public void showDatePickerDialog(View view) {
        final Button button = (Button) view;
        new DatePickerFragment() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String text = String.format("%04d-%02d-%02d", year, monthOfYear, dayOfMonth);
                button.setText(text);
            }

        }.show(getSupportFragmentManager(), "datePicker");
    }

    public void showImagePickerDialog(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void showTimePickerDialog(View view) {
        final Button button = (Button) view;
        new TimePickerFragment() {

            @Override
            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                String text = String.format("%02d:%02d", hourOfDay, minute);
                button.setText(text);
            }

        }.show(getSupportFragmentManager(), "timePicker");
    }

}
